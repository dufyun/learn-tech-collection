<%@ page contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<head>
	<title>agent page</title>
	<script src="${pageContext.request.contextPath}/recourse/jquery.js"></script>
</head>
<body>
	<div>
		<input type="text" placeholder="请输入wsSocketid" id="webSocketId" value="${wsId}"/><br/>
		<input type="text" placeholder="请输入发送的内容" id="message"/><br/>
		<button id="sendMsg">发送</button>
		<button id="resetMessage">清空发送内容</button>
		<button id="resetid">重置id</button>
	</div>

</body>
<script>

	$("#sendMsg").click(function () {
		var webSocketId = $("#webSocketId").val();
		var message = $("#message").val();
		var url = "${pageContext.request.contextPath}/websocket/notifyMsg";
		$.post(url,{"webSocketId":webSocketId,"message":message},function (data) {
			console.log(data);
			alert(data);

        })
    })
	$("#resetid").click(function () {
        $("#webSocketId").val("");
    })
	$("#resetMessage").click(function () {
        $("#message").val("");
    })

</script>
</html>
