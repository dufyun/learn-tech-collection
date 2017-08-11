<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>获取图片验证码</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery-1.10.2.min.js"></script>
</head>
<body>

	<form action="##" method='post'>
			<input type="hidden" id="userId" name="userId" value=""> 
				<div class="form-group">
					<div class="email controls">
						<input type="text" name='loginName' id="loginName" placeholder="用户名" value="" class='form-control'/>
					</div>
				</div>
				<div class="form-group">
					<div class="pw controls">
						<input type="password" autocomplete="off" id="pwd" name="pwd" placeholder="密码" class='form-control'/>
					</div>
				</div>
				
				<div class="form-group">
					<div class="email controls">
						<input id="validateCode" onblur="checkImg(this.value)" name="validateCode" type="text" class="form-control" placeholder="输入验证码"/>	
					</div>
					<span class="y_yzimg"><img id="codeValidateImg"  onClick="javascript:flushValidateCode();"/></span>
					<p class="y_change"><a href="javascript:flushValidateCode();"  >换一张</a></p>
				</div>
				
				<div class="form-group">
					<span class="text-danger"></span>
				</div>
				
				<div class="submit">
					<div class="remember">
							
								<input type="checkbox" name="remember" value="1" class='icheck-me' data-skin="square" data-color="blue" id="remember">
							
						<label for="remember">记住我</label>
					</div>
					<input type="button" value="登录" onclick="javascript:submitForm();" class='btn btn-primary'>
				</div>
			</form>
	
<script type="text/javascript">
$(document).ready(function() {
	 flushValidateCode();
   });

/* 刷新验证码 */
function flushValidateCode(){
var validateImgObject = document.getElementById("codeValidateImg");
validateImgObject.src = "${pageContext.request.contextPath }/getSysManageLoginCode?time=" + new Date();
}

function checkImg(code){
	var url = "${pageContext.request.contextPath}/checkimagecode";
	$.get(url,{"validateCode":code},function(data){
		if(data=="ok"){
			alert("ok!")
		}else{
			alert("error!")
			flushValidateCode();
		}
	})
}

</script>

</body>
</html>