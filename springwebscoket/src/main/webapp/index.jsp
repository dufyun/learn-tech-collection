<%@ page contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<body>
<h2>Hello World!</h2>
<a href="${pageContext.request.contextPath}/index" target="_blank">访客登录</a>
<a href="${pageContext.request.contextPath}/agent" target="_blank">客服登录</a>
<p>实现了访客可以发送消息到服务器，客服可以发送消息到访客端</p>
<p>客服端接收不到访客的消息，这里功能没有处理！</p>
</body>
</html>
