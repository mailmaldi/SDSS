package com.sdss.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Abhishek Sugandhi (abhisheks@webaroo.com)
 * @created 24-Aug-2009
 */
public class ScriptRunner extends Thread
{
	private ServerSocket socket;

	private ExecutorService executorService;

	private ScriptRunner()
	{
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));

		String portStr = System.getProperty("port");
		int port = 19999;

		try
		{
			port = Integer.parseInt(portStr.trim());
		}
		catch (Exception e)
		{
			// ignore
		}

		try
		{
			socket = new ServerSocket(port);
			executorService = Executors.newFixedThreadPool(25);
			System.out.println(new Date().toString() + " ScriptRunner started on port " + port);
		}
		catch (Exception e)
		{
			System.out.println(new Date().toString() + " ScriptRunner cannot be started on port " + port);
			e.printStackTrace();
		}
	}

	public void run()
	{
		while (true)
		{
			try
			{
				executorService.submit(new ScriptRunnerConnectionThread(socket.accept()));
			}
			catch (IOException e)
			{
				System.out.println(new Date().toString() + " " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static class ScriptRunnerConnectionThread implements Runnable
	{
		Socket socket;

		public ScriptRunnerConnectionThread(Socket socket)
		{
			this.socket = socket;
		}

		public void run()
		{
			ObjectInputStream in = null;
			ObjectOutputStream out = null;

			long threadId = 0;
			try
			{
				threadId = Thread.currentThread().getId();
				System.out.println(new Date().toString() + " [" + threadId + "] STARTED");
				in = new ObjectInputStream(socket.getInputStream());
				HashMap<String, Object> map = (HashMap<String, Object>) in.readObject();
				if ("executeScriptFile".equals(map.get("method")))
				{
					String scriptFileName = (String) map.get("scriptFileName");
					String outFileName = (String) map.get("outFileName");
					boolean append = (Boolean) map.get("append");

					Object obj = map.get("executeModified");
					boolean executeModified = (obj != null) ? (Boolean) obj : false;

					StringBuilder requestParams = new StringBuilder();
					requestParams.append("method=executeScriptFile").append(",");
					requestParams.append("scriptFileName=" + scriptFileName).append(",");
					requestParams.append("outputFileName=" + outFileName).append(",");
					requestParams.append("append=" + append);
					System.out.println(new Date().toString() + " [" + threadId + "] Got Request:" + requestParams);

					boolean isSuccessful = executeModified ? executeScriptFileModified(outFileName, append, new File(scriptFileName)) : executeScriptFile(outFileName, append,
							new File(scriptFileName));
					System.out.println(new Date().toString() + " [" + threadId + "] Sending Response:" + isSuccessful);

					map = new HashMap<String, Object>();
					map.put("isSuccessful", isSuccessful);
					out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject(map);
				}
				else if ("createScriptFile".equals(map.get("method")))
				{
					String name = (String) map.get("name");
					String extension = (String) map.get("extension");
					String executable = (String) map.get("executable");
					String script = (String) map.get("script");

					StringBuilder requestParams = new StringBuilder();
					requestParams.append("method=createScriptFile").append(",");
					requestParams.append("name=" + name).append(",");
					requestParams.append("extension=" + extension).append(",");
					requestParams.append("executable=" + executable).append(",");
					requestParams.append("script=" + script).append(",");
					System.out.println(new Date().toString() + " [" + threadId + "] Got Request:" + requestParams);

					boolean isSuccessful = true;
					File scriptFile = createScriptFile(name, extension, executable, script);
					System.out.println(new Date().toString() + " [" + threadId + "] Sending Response:" + isSuccessful);

					map = new HashMap<String, Object>();
					map.put("isSuccessful", isSuccessful);
					map.put("scriptFileName", scriptFile.getAbsolutePath());
					out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject(map);
				}
			}
			catch (Exception e)
			{
				System.out.println(new Date().toString() + " Exception: " + e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				close(in);
				close(out);
				close(socket);
				System.out.println(new Date().toString() + " [" + threadId + "] FINISHED");
			}
		}

		private File createScriptFile(String name, String extension, String executable, String script) throws IOException
		{
			File scriptFile = File.createTempFile(name, extension);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(scriptFile, false), "ISO-8859-1"));
			writer.write("#!" + executable + "\n");
			writer.write(script);
			writer.close();
			String commandArray[] = { "/bin/chmod", "+x", "" };
			commandArray[2] = scriptFile.getPath();
			try
			{
				Runtime.getRuntime().exec(commandArray).waitFor();
			}
			catch (InterruptedException e)
			{ /* ignore */
			}
			return scriptFile;
		}

		private boolean executeScriptFile(String outFile, boolean append, File scriptFile) throws IOException
		{
			boolean isSuccessful = true;

			// execute
			String commandArray[] = new String[] { "/bin/bash", "-c", new StringBuilder(64).append(scriptFile.getPath()).append(append ? " >> " : " > ").append(outFile).toString() };
			Process process = Runtime.getRuntime().exec(commandArray);
			InputStream in = process.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				System.out.println(new Date().toString() + " [" + Thread.currentThread().getId() + "]" + "[SCRIPTERR]" + line);
			}
			try
			{
				if (process.waitFor() != 0)
					isSuccessful = false;
			}
			catch (InterruptedException e)
			{ /* ignore */
			}

			// return the output for processing
			return isSuccessful;
		}

		/**
		 * This function will append the stderr as well as stdout into the output file & ignore exit condition for a successful return
		 * 
		 * @param outFile
		 * @param append
		 * @param scriptFile
		 * @return
		 * @throws IOException
		 */
		private boolean executeScriptFileModified(String outFile, boolean append, File scriptFile) throws IOException
		{
			boolean isSuccessful = true;

			// execute
			String commandArray[] = new String[] { "/bin/bash", "-c", new StringBuilder(64).append(scriptFile.getPath()).toString() };
			Process process = Runtime.getRuntime().exec(commandArray);

			InputStream in = process.getErrorStream();
			InputStream stdout = process.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
			BufferedReader outReader = new BufferedReader(new InputStreamReader(stdout, "ISO-8859-1"));

			BufferedWriter bWriter = new BufferedWriter(new FileWriter(outFile, append));

			String line = null;
			while ((line = reader.readLine()) != null)
			{
				bWriter.write(line);
				bWriter.newLine();
			}
			while ((line = outReader.readLine()) != null)
			{
				bWriter.write(line);
				bWriter.newLine();
			}
			bWriter.flush();
			bWriter.close();
			outReader.close();
			reader.close();
			try
			{
				process.waitFor();
			}
			catch (InterruptedException e)
			{ /* ignore */
			}

			// return the output for processing
			return isSuccessful;
		}

	}

	private static void close(Closeable closeable)
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

	private static void close(Socket socket)
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

	public static void main(String[] args)
	{
		new ScriptRunner().start();
	}
}
