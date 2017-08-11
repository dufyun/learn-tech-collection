<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery-1.10.2.min.js"></script>
</head>
<body>
	<h1>测试乱码</h1>
	<hr>
		  <div>
		  
		  	姓名：<input type="text" id="name" name="name"/>
		  	<button id="btn" onclick="submit()">提交</button>
		  	
		  </div>
		  
	<hr/>
	<%-- <mytag:hello age="${12+15 }" name="Jack"/> --%>
	
	<script type="text/javascript">
	
		$(document).ready(function(){
			$.ajax({
				url:'https://ipoll.sycm.taobao.com/live/source/getCompetitorFlowSource.json?competitorUserId=1124593616&device=1&limit=10&page=1&sycmToken=0e2729575&_=146695004431312',
			    type:'GET',
			    dataType:'json',
			    success:function(data){
			    	//alert("success" +data);
			    	console.log(data);
			    },
			    error:function(data){
			    	//alert("error" +data);
			    	console.log(data);
			    }
			})
			
		});
			
		function submit(){
			
			//var name = document.getElementById("name").value;
			var name = $("#name").val();
			alert(name);
			 if(name != null && name != ""){
				window.location.href="${pageContext.servletContext.contextPath}/testName?name=" +encodeURI(name);
			}else{
				alert("error");
			} 
			
		}
		
		
	
	</script>	
</body>
</html>
