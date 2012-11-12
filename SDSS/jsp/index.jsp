<!--index.jsp-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<!-- ..//JQuery Source\\.. -->
<script type="text/javascript" src="jquery-1.8.2.min.js"></script>
<!-- ..//JavaScript Code for this page\\.. -->
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#login_frm")
								.submit(
										function() {

											//remove previous class and add new "myinfo" class
											$("#msgbox")
													.removeClass()
													.addClass('myinfo')
													.text(
															'Validating Your Login ')
													.fadeIn(1000);

											this.timer = setTimeout(
													function() {
														$
																.ajax({
																	url : 'check.jsp',
																	data : 'un='
																			+ $(
																					'#login_id')
																					.val()
																			+ '&pw='
																			+ $(
																					'#password')
																					.val(),
																	type : 'post',
																	success : function(
																			msg) {
																		obj = JSON
																				.parse(msg
																						.trim());

																		if (obj.status == 'success') // Message Sent, check and redirect
																		{
																			$(
																					"#msgbox")
																					.html(
																							'Login Verified, Logging in.....')
																					.addClass(
																							'myinfo')
																					.fadeTo(
																							900,
																							1,
																							function() {
																								//redirect to secure page
																								document.location = 'login.jsp'; //?user='+msg;
																							});

																		} else {
																			$(
																					"#msgbox")
																					.fadeTo(
																							200,
																							0.1,
																							function() //start fading the messagebox
																							{
																								//add message and change the class of the box and start fading
																								$(
																										this)
																										.html(
																												'Sorry, Wrong Combination Of Username And Password.')
																										.removeClass()
																										.addClass(
																												'myerror')
																										.fadeTo(
																												900,
																												1);
																							});

																		}
																	}

																});
													}, 200);
											return false;
										});

					});
</script>

<link href="style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Form</title>
</head>
<body>

	<form name="login_frm" id="login_frm" action="" method="post">
		<div id="login_box">
			<div id="login_header">Login</div>
			<div id="form_val">
				<div>User Id :</div>
				<div>
					<input type="text" name="login_id" id="login_id" /><span
						style="font-size: 10px;">hint : admin</span>
				</div>

				<div>Password:</div>
				<div>
					<input type="password" name="password" id="password" /><span
						style="font-size: 10px;">hint : test</span>
				</div>
				<div style="clear: both; height: 0px;"></div>

				<div id="msgbox"></div>
			</div>
			<div id="login_footer">
				<label> <input type="submit" name="login" id="login"
					value="Login" />
				</label>
			</div>
		</div>
	</form>

</body>
</html>

