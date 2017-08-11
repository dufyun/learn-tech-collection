
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Test validate</title>
    <script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/static/js/jquery.validate.min.js"></script>
  </head>
  <body>
  <a href="${pageContext.request.contextPath}/login.do">请使用你的QQ账号登陆</a>
	<form id="mainForm"  action="${pageContext.servletContext.contextPath}/usercenter/tclcustomer/changepwdtcl" method="post">
					<input type="hidden" name="customerUuid" value="${m.uuid}">
					<input type="hidden" name="pwdStrength">
					<div class="y_SettingContent">
						<div class="form-group">
							<label class="control-label"><aebiz:showTitle titleId="usercenter.changeLoginPassword.originalPwd"/></label>
							<div class="coltrol-box">
							    <input type="password" autocomplete="off" id="password" name="password" class="form-control">
							</div>
						</div>
						<div class="form-group y_newpassword">
							<label class="control-label"><aebiz:showTitle titleId="usercenter.changeLoginPassword.newdPwd"/></label>
							<div class="coltrol-box">
								<input type="password" id="newpwd" name="newpwd" class="form-control passwordreg" placeholder="<aebiz:showTitle titleId="ucenter_changepwd_newPwdTip"/>">
								<div id="pwdpower" class="paddword_leve paddword_leve_3" > 
									<span id="pweak" style=""><aebiz:showTitle titleId="system.m.pweak"/></span>
									<span id="pmedium" style=""><aebiz:showTitle titleId="system.m.pmedium"/></span>
									<span id="pstrong" style=""><aebiz:showTitle titleId="system.m.pstrong"/></span>
								</div>	
							</div>
						</div>
						<div class="form-group">
							<label class="control-label"><aebiz:showTitle titleId="usercenter.changeLoginPassword.newdPwdSecond"/></label>
							<div class="coltrol-box">
								<input type="password" autocomplete="off" id="confirmpwd" name="confirmpwd" class="form-control">
							</div>
						</div>										
					</div>
					<p>
					    <button type="submit"  class="btn btn-custom2"><aebiz:showTitle titleId="basebusiness.showmessage.save"/></button>
                        <a href="javascript:cancelUpdate();" class="btn btn-custom"><aebiz:showTitle titleId="usercenter.changeLoginPassword.cancel"/></a>
 					</p>
					</form>			
  </body>
</html>

<script>
$("#mainForm").validate({
	 rules: {
  		 	password: {
			required : true
			//remote : {
			//	url: "${pageContext.request.contextPath }/usercenter/tclcustomer/checkPwd?num="+Math.random(), 
			//	type:"get",
			//	dataType:"json",
			//	data:{
			//		customerUuid : customerUuid,
			//		password : function(){
			//			return $("#password").val();
			//		}
			//	}
			//}
		},
		newpwd:{
			required : true 
		},
		confirmpwd:{
			required : true ,
			equalTo : "#newpwd"
		},
		code:{
			required : true 
			//remote :{
			//	url: "${pageContext.servletContext.contextPath}/usercenter/customer/checkValidateCode",
			//	type:"get",
			//	dataType:"json",
			//	data:{
			//		mobileOrEmail : mobileOrEmail,
			//		code : function(){
			//			return $("#code").val();
			//		}
			//	}
			//}
		}
  		},
        messages: {
   			password: {
			required : "<aebiz:showTitle titleId="ucenter_changepwd_passwordTip"/>" , 
			remote : "<aebiz:showTitle titleId="ucenter_changepwd_wrongPassword"/>"
		},
		newpwd:{
			required : "<aebiz:showTitle titleId="ucenter_changepwd_newPwdTip"/>"
		},
		confirmpwd:{
			required : "<aebiz:showTitle titleId="ucenter_changepwd_confirmPwdTip"/>" ,
			equalTo :"<aebiz:showTitle titleId="ucenter_changepwd_wrongConfirmPwd"/>"
		},
		code:{
			required : "<aebiz:showTitle titleId="ucenter_changepwd_codeTip"/>" ,
			remote : "<aebiz:showTitle titleId="ucenter_changepwd_wrongCode"/>"
		}
  		},

	errorElement: 'span',  //输入错误时的提示标签
	errorClass: 'help-block has-error',  //输入错误时的提示标签类名
	errorPlacement: function(error, element) {  //输入错误时的提示标签显示的位置
        if(element.parents(".input-group").length > 0){
        		element.parents(".input-group").after(error);
        }else if(element.parents(".y_validatainput").length > 0){
        		element.parents(".y_validatainput").after(error);
        }else if(element.parents("label").length > 0) {
            element.parents("label").after(error);
        }else if(element.parents(".num-validate").length > 0){
            		element.parents(".num-validate").append(error);
        }else {
            element.after(error);
        }
	},
	
   highlight: function(label) {   //输入错误时执行的事件
        $(label).closest('.form-group').removeClass('has-error has-success').addClass('has-error');
   },

	success: function(label) {   //输入正确时执行的事件
		label.addClass('valid').closest('.form-group').removeClass('has-error has-success').addClass('has-success');
	},

	onkeyup: function(element) {   //验证元素输入值时按钮松开执行的事件
		$(element).valid();
	},
	
	onfocusout: function(element) {   //验证元素失去焦点时进行验证
	 		$(element).valid();
	}
});
</script>