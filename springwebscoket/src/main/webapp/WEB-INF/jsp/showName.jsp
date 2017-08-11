<%@ page contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<head>
	<title>show name</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/login" method="post">
		用户名:
		<select name="id">
			<option value="1">张三</option>
			<option value="2">李四</option>
		</select><br>
		密码:
		<input name="password" type="text" value="123456">
		<input type="submit" value="登录">
	</form>
</body>
</html>
