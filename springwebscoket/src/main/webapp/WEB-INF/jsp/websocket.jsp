<%--
  Created by IntelliJ IDEA.
  User: dufy
  Date: 2017/7/28
  Time: 11:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getServerName() + ":"
            + request.getServerPort();
    String finalPath = "http://" + basePath + path;
    out.print("finalPath = " + finalPath);
%>
<html>
<head>
    <title>Hello WebSocket</title>
  <%--  <script src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.min.js"></script>
    <script src="http://cdn.bootcss.com/stomp.js/2.3.3/stomp.js"></script>
    <script src="http://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>--%>
    <script src="${pageContext.request.contextPath}/recourse/jquery-1.7.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/recourse/stomp.js"></script>
    <script src="${pageContext.request.contextPath}/recourse/sockjs.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            connect();
            //checkoutUserlist();
        });

        var projectName = "springwebscoket";
        var baseUrl= getBaseUrl();
        var stompClient = null;
        console.log("baseUrl = " + baseUrl);
        //this line.
        function connect() {
            console.log('Connected: ----------------------------------');
            var userid = document.getElementById('name').value;

            var socket = new SockJS(baseUrl + projectName +"/chat");
            console.log("=-="+ socket);
            stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                setConnected(true);
                console.log('Connected: ' + frame);

                stompClient.ws.onclose = function (CloseEvent){
                    console.log("ERROR","weboscket close code is " + CloseEvent.code);
                    setConnected(false);
                };

                //系统订阅消息
                stompClient.subscribe('/user/queue/system/newMsg', function (greeting) {
                    console.log("------ue/system/newMsg-----------");
                    console.log(greeting);
                    showGreeting(JSON.parse(greeting.body).content);
                });

                //新消息订阅
                stompClient.subscribe('/user/queue/chat/newMsg', function (greeting) {
                    console.log("/chat/newMsg" + greeting);
                    $("#showMessage").append("<p>"+JSON.parse(greeting.body).message+"</p>")
                    //showGreeting(JSON.parse(greeting.body).content);
                });

                //访客消息响应订阅
                stompClient.subscribe('/user/queue/chat/msgResponse', function (greeting) {
                    alert("收到信息");
                });

                //关闭订阅消息
                stompClient.subscribe('/user/queue/system/close', function (greeting) {
                    var retCode = JSON.parse(greeting.body).retCode;
                    if(retCode == "000000"){
                        _evaluate.open();
                        disconnect();
                    }
                });


            },function(error){
                var msg = "会话建立失败，请稍候重试！";
                alert(msg);
                setConnected(false);

            });
        }



        function sendName() {
            var name = document.getElementById('name').value;
            stompClient.send("/app/sendMessage", {}, JSON.stringify({ 'name': name }));
        }
    /*
        function connectAny() {
            var socket = new SockJS("http://localhost:8080/springmvc/hello");
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/feed', function(greeting){
                    alert(JSON.parse(greeting.body).content);
                    showGreeting(JSON.parse(greeting.body).content);
                });
            });
        }
    */
        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }


        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
            document.getElementById('response').innerHTML = '';
        }

        function showGreeting(message) {
            var response = document.getElementById('response');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(message));
            response.appendChild(p);
        }


        /**
         * 获取baseUrl
         * @returns {string}
         */
        function getBaseUrl(){
            var url= window.location.href;
            var num = url.indexOf(projectName);
            var baseUrlStr = url.substring(0,num);
            return baseUrlStr;
        }
    </script>
</head>
<body>
<h1>Welcome</h1> ${name }<h1>访问此页面</h1>
<div>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="connectAny" onclick="connectAny();">ConnectAny</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <div id="conversationDiv">
        <label>you can send message to WebSocketMessageController[ @MessageMapping("/sendMessage") ]</label><input type="text" id="name" />
        <button id="sendName" onclick="sendName();">Send</button>
        <p id="response"></p>
    </div>

    <div id="showMessage">

    </div>

</div>
</div>
</body>
</html>
