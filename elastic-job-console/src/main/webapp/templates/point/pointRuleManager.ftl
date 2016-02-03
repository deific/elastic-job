<#assign base=springMacroRequestContext.getContextUrl("")> 
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.0/bootstrap-table.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.0/bootstrap-table.min.js"></script>
<!-- Latest compiled and minified Locales -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.0/locale/bootstrap-table-zh-CN.min.js"></script>

<div class="row wrapper white-bg page-heading">
    <div class="col-lg-12">
        <h2>积分规则</h2>
        <ol class="breadcrumb">
            <li><a>积分</a></li>
            <li class="active"><b>积分规则</b></li>
        </ol>
    </div>
</div> 
<div class="wrapper wrapper-content animated fadeInRight">
	<div id="toolbar">
        <a class="btn btn-primary" href="${base}/rule/add" role="button">
            <i class="glyphicon glyphicon-plus"></i> 新增
        </a>
    </div>
	<table id="ruleTableList"></table>
    <div id="ruleInfoWindow" class="modal fade">
	    <div class="modal-dialog modal-lg">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                <h4 class="modal-title">积分明细</h4>
	            </div>
	            <div class="modal-body">
	                <p>
	                	<table id="pointDetailTableList"></table>
	                </p>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	            </div>
	        </div>
	    </div>
	</div>
</div>
<script>
    	$(document).ready(function() {
    		initPointTable('ruleTableList');
    		initDetailTable();
    	});
        // 初始化
        function initPointTable(table) {
        	$('#' + table).bootstrapTable({
        		url: "${base}/rule/list",
        		method: 'post',
        		toolbar:"#toolbar",
				striped: true,
				dataType: "json",
				pagination: true,
				queryParamsType: "",
				singleSelect: false,
				pageSize: 10,
				pageNumber:1,
				idField:"id",
				cardView:false,
				showToggle:true,
				detailView:true,
				detailFormatter:showDetail,
				search: true, //显示 搜索框
				showColumns: true, //不显示下拉框（选择显示的列）
				sidePagination: "server", //服务端请求
				queryParams: queryParams,
				minimunCountColumns: 2,
				responseHandler: responseHandler,
	            columns: [{
	                        title: 'ID',
	                        field: 'id',
	                        align: 'center',
	                        valign: 'middle'
	                    },{
	                        title: '名称',
	                        field: 'name',
	                        align: 'left',
	                        valign: 'middle'
	                    }, {
	                        title: '行为编码',
	                        field: 'behaviorCode',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '优先级',
	                        field: 'priority',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '规则表达式',
	                        field: 'ruleExpression',
	                        align: 'center',
	                        valign: 'middle'
	                    },{
	                        title: '规则值',
	                        field: 'value',
	                        align: 'center',
	                        valign: 'middle'
	                    },{
	                        title: '状态',
	                        field: 'state',
	                        align: 'center',
	                        valign: 'middle'
	                    },{
	                        title: '操作',
	                        formatter: showOperation,
	                        align: 'center',
	                        valign: 'middle'
	                    }
	            	]
        	});
        }
        function initDetailTable() {
        }
        function showOperation(value,row,index) {
        	return [
        		"<a class='btn btn-primary btn-xs' href='${base}/rule/modify?id=" + row.id + "' role='button' style='font-size: 10px'><i class='glyphicon glyphicon-edit'></i> 修改</a>",
        		"<a class='btn btn-warning btn-xs' href='#' role='button' onclick='onclick='deleteRule(\"" + row.userId + "\")' style='font-size: 10px'><i class='glyphicon glyphicon-remove'></i> 删除</a>"
        	].join(' ');
        }
        function showDetail(index, row, element) {
        	return  "<table class='table table-striped table-bordered table-condensed'>" +
        			"<tr><td class='text-right'>描述</td><td colspan='3'>" + row.desc + "</td></tr>" + 
        	 		"<tr><td class='text-right'>区域编码</td><td>" + row.areaCode + "</td><td class='text-right'>代理商编码</td><td>" + row.agentCode + "</td></tr>" + 
        			"<tr><td class='text-right'>诊所编码</td><td>" + row.clinicCode + "</td><td class='text-right'>用户类别</td><td>" + row.userClass + "</td></tr>" + 
        			"<tr><td class='text-right'>用户类型</td><td>" + row.userType + "</td><td class='text-right'>是否排他</td><td>" + row.exclusive + "</td></tr>" + 
        			"<tr><td class='text-right'>计算模式</td><td>" + row.ruleMode + "</td><td class='text-right'>规则消息</td><td>" + row.ruleMessage + "</td></tr>" + 
        			"<tr><td class='text-right'>每日上限</td><td>" + row.maxValue + "</td><td class='text-right'>周期</td><td>" + row.scope + "</td></tr>" +
        			"<tr><td class='text-right'>生效时间</td><td>" + row.effectiveTime + "</td><td class='text-right'>失效时间</td><td>" + row.expireTime + "</td></tr>" + 
        			"<tr><td class='text-right'>创建时间</td><td colspan='3'>" + row.createTime + "</td></tr></table>"
        			;
        }
        
        function editRule(ruleId) {
        	$("#ruleInfoWindow").modal('show');
        }
        function responseHandler(res) {
        	if (res.rtnCode == 0) {
        		return res.data;
        	}
		}
		
		//传递的参数
		function queryParams(params) {
			return {
			 	pageSize:params.pageSize,
				page:params.pageNumber,
				clinicCode: params.searchText
			};
		}
		
		//传递的参数
		function queryDetailParams(params) {
			return {
			 	pageSize:params.pageSize,
				page:params.pageNumber,
			};
		}
		function getPointType(pointType) {
			if ("0" == pointType) {
				return "产生";
			}
			if ("1" == pointType) {
				return "消费";
			}
		}
    </script>
