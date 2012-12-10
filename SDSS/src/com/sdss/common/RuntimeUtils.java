package com.sdss.common;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class RuntimeUtils
{

	private static String remoteHost = "localhost";

	private static int remotePort = 19999;

	public static File executeBashScript(String script, String outFileName, boolean append) throws IOException
	{
		return executeBashScriptWithErr(script, outFileName, append, false, remoteHost, remotePort);
	}

	public static File executeBashScriptWithErr(String script, String outFileName, boolean append, boolean runModified, String host, int port) throws IOException
	{
		// create a script file & make it executable
		File scriptFile = createScriptFileRemote("bash", ".sh", "/bin/bash", script, host, port);

		if (scriptFile == null)
			throw new IOException("cannot create script file while executing bash script");
		// execute the script & redirect the output to new file
		return executeScriptFileWithErrRemote(outFileName, append, scriptFile, runModified, host, port);
	}

	public static File executePythonScript(String script, String outFileName, boolean append) throws IOException
	{
		return executePythonScriptWithErr(script, outFileName, append, false, remoteHost, remotePort);
	}

	public static File executePythonScriptWithErr(String script, String outFileName, boolean append, boolean runModified, String host, int port) throws IOException
	{
		// create a script file & make it executable
		File scriptFile = createScriptFileRemote("python", ".py", "/usr/bin/python", script, host, port);

		if (scriptFile == null)
			throw new IOException("cannot create script file while executing python script");
		// execute the script & redirect the output to new file
		return executeScriptFile(outFileName, append, scriptFile);
	}

	public static File createScriptFile(String name, String extension, String executable, String script) throws IOException
	{
		return createScriptFileRemote(name, extension, executable, script, remoteHost, remotePort);
	}

	public static File createScriptFileRemote(String name, String extension, String executable, String script, String host, int port) throws IOException
	{
		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put("method", "createScriptFile");
		input.put("name", name);
		input.put("extension", extension);
		input.put("executable", executable);
		input.put("script", script);
		HashMap<String, Object> output = runtimeRPC(input, host, port);
		return Boolean.TRUE.equals(output.get("isSuccessful")) ? new File((String) output.get("scriptFileName")) : null;
	}

	public static File executeScriptFile(String outFileName, boolean append, File scriptFile) throws IOException
	{
		return executeScriptFileWithErr(outFileName, append, scriptFile, false);
	}

	public static File executeScriptFileWithErr(String outFileName, boolean append, File scriptFile, boolean runModified) throws IOException
	{
		return executeScriptFileWithErrRemote(outFileName, append, scriptFile, runModified, remoteHost, remotePort);
	}

	public static File executeScriptFileWithErrRemote(String outFileName, boolean append, File scriptFile, boolean runModified, String host, int port) throws IOException
	{
		if (scriptFile == null || outFileName == null)
			throw new IllegalArgumentException("invalid arguments scriptFile/outFileName");

		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put("method", "executeScriptFile");
		input.put("scriptFileName", scriptFile.getAbsolutePath());
		input.put("outFileName", outFileName);
		input.put("append", Boolean.valueOf(append));
		input.put("executeModified", runModified);
		HashMap<String, Object> output = runtimeRPC(input, host, port);
		return Boolean.TRUE.equals(output.get("isSuccessful")) ? new File(outFileName) : null;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<String, Object> runtimeRPC(HashMap<String, Object> input, String host, int port) throws IOException
	{
		HashMap<String, Object> output = new HashMap<String, Object>();

		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try
		{
			socket = new Socket(host, port);
			socket.setSoTimeout(0);

			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(input);

			in = new ObjectInputStream(socket.getInputStream());
			return (HashMap<String, Object>) in.readObject();
		}
		catch (ClassNotFoundException e)
		{/* ignore */
		}
		finally
		{
			close(in);
			close(out);
			close(socket);
		}

		return output;
	}

	public static void close(Closeable closeable)
	{
		try
		{
			if (closeable != null)
				closeable.close();
		}
		catch (IOException e)
		{/* ignore */
		}
	}

	public static void close(Socket socket)
	{
		try
		{
			if (socket != null)
				socket.close();
		}
		catch (IOException e)
		{/* ignore */
		}
	}

	public static void main(String[] args) throws IOException
	{

		System.out.println(RuntimeUtils.executeBashScriptWithErr("mysql 'select phoneNo from Users where phoneNo like \"91%\" limit 20'", "/tmp/result_sh.txt", false, true,
				"localhost", 19999));
		// System.out.println(executePythonScript("print 'hello world'", "/tmp/result_py.txt", false));
	}
}
