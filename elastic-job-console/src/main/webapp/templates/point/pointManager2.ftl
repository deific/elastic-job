<#assign base=springMacroRequestContext.getContextUrl("")>  
<div class="wrapper wrapper-content animated fadeInRight">
	<form class="form-horizontal" role="form">
	 <fieldset>
       <div class="form-group">
          <label class="col-sm-2 control-label" for="user_id">用户ID</label>
          <div class="col-sm-4">
             <input class="form-control" id="userId" name="userId" type="text" value="${(queryFrom.userId)!}" placeholder="请输入用户ID"/>
          </div>
          <label class="col-sm-2 control-label" for="clinic_code">诊所编号</label>
          <div class="col-sm-4">
             <input class="form-control" id="clinicCode" name="clinicCode" type="text" value="${(queryFrom.clinicCode)!}" placeholder="请输入诊所编号"/>
          </div>
          <div class="col-sm-4">
             <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="${base}/point/manager"/>
          </div>
       </div>
    </fieldset>  
				
	</form>
	
	<table id="jobs-overview-tbl" class="table table-striped table-bordered table-hover table-condensed">
	    <thead>
	        <tr>
	            <th>用户ID</th>
	            <th>用户类型</th>
	            <th>诊所编号</th>
	            <th>积分余额</th>
	            <th>积分年度</th>
	            <th>更新时间</th>
	        </tr>
	    </thead>
	    <tbody>
	    <#list userPointPage as userPoint>
	        <tr>
	            <td>${userPoint.userId!}</td>
	            <td>${userPoint.userType!}</td>
	            <td>${userPoint.clinicCode!}</td>
	            <td>${userPoint.allPoint!}</td>
	            <td>${userPoint.year!}</td>
	            <td>${userPoint.updateTime?datetime!}</td>
	        </tr>
	    </#list>
	    </tbody>
	    <tfoot>
	    <tr>
	        <td colspan="6">
	            <ul class="pagination">
				  <li  ><a href="${base!}/point/manager?page=1">&laquo;</a></li>
				  <li <#if page == 1>class="active"</#if>><a href="${base!}/point/manager?page=1">1</a></li>
				  <li <#if page == 2>class="active"</#if>><a href="${base!}/point/manager?page=2">2</a></li>
				  <li <#if page == 3>class="active"</#if>><a href="${base!}/point/manager?page=3">3</a></li>
				  <li <#if page == 4>class="active"</#if>><a href="${base!}/point/manager?page=4">4</a></li>
				  <li <#if page == 5>class="active"</#if>><a href="${base!}/point/manager?page=5">5</a></li>
				  <li ><a href="${base!}/point/manager?page=${totalPage}">&raquo;</a></li>
				  <span>共${total!}条记录，每页展示${pageSize!}条</span>
				</ul>
	        </td>
	    </tr>
	    </tfoot>
	</table>
</div>