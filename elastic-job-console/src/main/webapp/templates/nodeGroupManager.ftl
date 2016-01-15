<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>节点组管理</h2>
        <ol class="breadcrumb">
            <li><a>节点管理</a></li>
            <li class="active"><b>节点组管理</b></li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-title">
                    <h3><span class="text-navy">节点组管理</span></h3>
                </div>
                <div class="ibox-content">
                    <form method="post" id="form" class="form-horizontal" onsubmit="return false">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">节点组</label>

                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="nodeGroup"
                                       placeholder="请输入节点组">
                            </div>
                            <label class="col-sm-1 control-label">节点类型</label>

                            <div class="col-sm-2">
                                <select name="nodeType" class="form-control">
                                    <option value="">所有</option>
                                    <option value="JOB_CLIENT">JobClient</option>
                                    <option value="TASK_TRACKER">TaskTracker</option>
                                </select>
                            </div>
                            <div class="col-sm-1" style="margin-left:10px;width:70px;">
                                <button class="btn btn-primary" type="button" id="searchBtn">
                                    搜索
                                </button>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-warning" type="button" id="addBtn">
                                    添加
                                </button>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content" id="ltstableContainer">
                </div>
            </div>
        </div>
    </div>
</div>

<script id="ltstable" type="text/html">
    <table class="table table-stripped toggle-arrow-tiny footable" data-page-size="10">
        <thead>
        <tr>
            <th data-toggle="true">节点类型</th>
            <th>节点组名</th>
            <th>节点组创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        {{each rows as row index}}
        <tr>
            <td>{{row.nodeType}}</td>
            <td>{{row.name}}</td>
            <td>{{row.gmtCreated | dateFormat:'yyyy-MM-dd HH:mm:ss'}}</td>
            <td>{{row.opt | format:'optFormat',row}}</td>
        </tr>
        {{/each}}
        {{if results == 0}}
        <tr>
            <td colspan="4">暂无数据</td>
        </tr>
        {{/if}}
        </tbody>
        <tfoot>
        <tr>
            <td colspan="4">
                <span>共{{results}}条记录，每页展示{{pageSize}}条</span>
                <ul class="pagination-sm pull-right"></ul>
            </td>
        </tr>
        </tfoot>
    </table>
</script>

<script>
    $(document).ready(function () {

        LTS.colFormatter.optFormat = function (v, row) {
            return '<a href="javascript:;" class="node-group-del-btn" nodeGroup="' + row.name + '" nodeType="' + row.nodeType + '">' +
                    '<span class="label label-primary" style="background-color: #DD6B55;"><i class="fa fa-trash-o"></i> 删除</span></a>';
        }
        var ltsTable = $("#ltstableContainer").ltsTable({
            url: 'api/node/node-group-get',
            templateId: 'ltstable'
        });

        $(document).on("click", "#searchBtn", function () {
            var params = {};
            $.each($('#form').parent().find(".form-control"), function () {
                var name = $(this).attr("name");
                var value = $(this).val();
                params[name] = value;
            });
            ltsTable.post(params, 1);
        });

        $(document).on("click", ".node-group-del-btn", function () {
            var nodeGroup = $(this).attr("nodeGroup");
            var nodeType = $(this).attr("nodeType");
            var _this = $(this);

            swal({
                title: "确认要删除该节点组吗？",
                text: "对应的该节点组的数据也将被删除，请谨慎操作 !",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认删除",
                closeOnConfirm: false
            }, function (isConfirm) {
                if(isConfirm){
                    $.ajax({
                        url: 'api/node/node-group-del',
                        type: 'POST',
                        dataType: 'json',
                        data: {nodeGroup: nodeGroup, nodeType: nodeType},
                        success: function (json) {
                            if (json && json.success) {
                                swal("删除成功!", "恭喜你", "success");
                                _this.parents("tr").remove();
                            } else {
                                json ? swal(json['msg']) : {};
                            }
                        }
                    });
                }
            });
        });

        $(document).on("click", "#addBtn", function(){
            var nodeGroup = $("input[name='nodeGroup']").val();
            var nodeType = $("select[name='nodeType']").val();
            if (!nodeGroup) {
                sweetAlert("请输入节点组名称", "节点组名称不能为空。", "error");
                return;
            }
            if (!nodeType) {
                sweetAlert("请选择节点类型", "节点类型不能为空。", "error");
                return;
            }
            $.ajax({
                url: 'api/node/node-group-add',
                type: 'POST',
                dataType: 'json',
                data: {nodeGroup: nodeGroup, nodeType: nodeType},
                success: function (json) {
                    if (json && json.success) {
                        $("input[name='nodeGroup']").val("");
                        $("select[name='nodeType']").val("");
                        $('#searchBtn').trigger("click");
                    } else {
                        json ? swal(json['msg']) : {};
                    }
                }
            });
        });

        $("#searchBtn").trigger("click");
    });
</script>