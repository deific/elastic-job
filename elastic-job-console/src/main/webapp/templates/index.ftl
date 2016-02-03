<#assign base=springMacroRequestContext.getContextUrl("")>

<!DOCTYPE html>
<html>
<head>
    <#include "./layout/head.ftl">
</head>
<body>


<div class="container">
	
	<div id="loginModal" class="modal show">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
				  <div class="loginBox row">
				  		<br/>
				  		<h2 class="text-center">Data Process Center 后台管理系统</h2>
				  		<br/>
				  		<br/>
				  		<br/>
						<form id="loginForm" name="loginForm" action="./login" method="post" class="form-horizontal">
						  <div class="form-group has-success">
						    <label for="nick_name" class="col-sm-4 col-md-4 control-label">用户名</label>
						    <div class="col-sm-4 col-md-4">
						      <input type="text" class="form-control" name="userName" placeholder="用户名" value="">
						    </div>
						  </div>
						  <div class="form-group has-success">
						    <label for="user_password" class="col-sm-4 col-md-4 control-label">密码</label>
						    <div class="col-sm-4 col-md-4">
						      <input type="password" class="form-control" name="password" placeholder="密码">
						    </div>
						  </div>
					  	  <div class="form-group">
					  	  	<div class="col-sm-offset-4 col-sm-4" style="color: #990033;"></div>
						  </div>
						  <div class="form-group">
						    <div class="col-sm-offset-4 col-sm-4 col-md-4">
						      	<button class="btn btn-primary" data-loading-text="正在登录..." type="submit">登 录</button>
						      	<button class="btn btn-info" type="reset">清 空</button>
						    </div>
						  </div>
				  		</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>