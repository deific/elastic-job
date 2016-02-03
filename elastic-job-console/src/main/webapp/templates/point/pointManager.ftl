<#assign base=springMacroRequestContext.getContextUrl("")> 
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.0/bootstrap-table.min.css">
<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.0/bootstrap-table.min.js"></script>
<!-- Latest compiled and minified Locales -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.10.0/locale/bootstrap-table-zh-CN.min.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>积分管理</h2>
        <ol class="breadcrumb">
            <li><a>积分</a></li>
            <li class="active"><b>积分管理</b></li>
        </ol>
    </div>
</div> 
<div class="wrapper wrapper-content animated fadeInRight">
	<div id="toolbar">
        <a class="btn btn-primary" href="${base}/point/add" role="button">
            <i class="glyphicon glyphicon-plus"></i> 新增
        </a>
    </div>
	<table id="pointTableList"></table>
    <div id="pointDetail" class="modal fade">
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
    		initPointTable('pointTableList');
    		initDetailTable();
    	});
        // 初始化
        function initPointTable(table) {
        	$('#' + table).bootstrapTable({
        		toolbar:"#toolbar",
        		url: "${base}/point/list",
        		method: 'post',
				striped: true,
				dataType: "json",
				pagination: true,
				queryParamsType: "",
				singleSelect: false,
				contentType: "application/x-www-form-urlencoded",
				pageSize: 10,
				pageNumber:1,
				search: true, //显示 搜索框
				showColumns: true, //不显示下拉框（选择显示的列）
				sidePagination: "server", //服务端请求
				queryParams: queryParams,
				//minimunCountColumns: 2,
				responseHandler: responseHandler,
	            columns: [
	                    {
	                        title: '用户ID',
	                        field: 'userId',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '用户类型',
	                        field: 'userType',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '诊所编号',
	                        field: 'clinicCode',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '积分余额',
	                        field: 'allPoint',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '积分年度',
	                        field: 'year',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '更新时间',
	                        field: 'updateTime',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '操作',
	                        formatter: showOperation,
	                        align: 'center',
	                        valign: 'middle'
	                    }
	            	]
        	});
        }
        function initDetailTable() {
        	$('#pointDetailTableList').bootstrapTable({
        		method: 'post',
				striped: true,
				dataType: "json",
				pagination: true,
				queryParamsType: "",
				singleSelect: false,
				contentType: "application/x-www-form-urlencoded",
				pageSize: 10,
				pageNumber:1,
				search: false, //显示 搜索框
				showColumns: false, //不显示下拉框（选择显示的列）
				sidePagination: "server", //服务端请求
				queryParams: queryDetailParams,
				responseHandler: responseHandler,
	            columns: [{
	                        title: '积分类型',
	                        field: 'pointType',
	                        formatter:getPointType,
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '积分维度',
	                        field: 'pointDimension',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '积分',
	                        field: 'point',
	                        align: 'center',
	                        valign: 'middle'
	                    }, {
	                        title: '描述',
	                        field: 'desc',
	                        align: 'left',
	                        valign: 'middle'
	                    }, {
	                        title: '时间',
	                        field: 'pointTime',
	                        align: 'center',
	                        valign: 'middle'
	                    }
	            	]
        	});
        }
        function showOperation(value,row,index) {
        	return "<input type='button' class='btn btn-primary btn-xs' data-toggle='modal' value='查看明细' style='font-size: 10px' onclick='showDetail(\"" + row.userId + "\")'/>";
        }
        function showDetail(userId) {
        	$('#pointDetailTableList').bootstrapTable('refresh',{
        			url: "${base}/point/detail/list?userId=" + userId,
        		});
        	
        	$("#pointDetail").modal('show');
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
