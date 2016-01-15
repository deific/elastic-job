<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-12">
        <h2>任务业务日志</h2>
        <ol class="breadcrumb">
            <li><a>日志</a></li>
            <li class="active"><b>任务业务日志</b></li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-title">
                    <h3>按 <span class="text-navy">任务ID</span> 查询</h3>
                </div>
                <div class="ibox-content">
                    <form method="post" class="form-horizontal" onsubmit="return false">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">任务ID</label>

                            <div class="col-sm-3">
                                <input type="text" class="form-control" name="taskId" value="$!taskId"
                                       placeholder="请输入你提交的任务ID(TaskId)">
                            </div>

                            <label class="col-sm-1 control-label">节点组</label>

                            <div class="col-sm-3">
                                <select class="form-control selectpicker" name="taskTrackerNodeGroup">
                                    <option value="">-- 不限 --</option>
                                    #foreach ($nodeGroup in $taskTrackerNodeGroups)
                                        #if ($taskTrackerNodeGroup == $nodeGroup.name)
                                            <option value="$nodeGroup.name" selected>$nodeGroup.name</option>
                                        #else
                                            <option value="$nodeGroup.name">$nodeGroup.name</option>
                                        #end
                                    #end
                                </select>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 control-label">时间</label>

                            <div class="col-sm-2">
                                <input class="form-control datepicker" type="text" style="width:160px"
                                       id="startLogTime"
                                       name="startLogTime"
                                       date-format="yyyy-MM-dd HH:mm:ss"
                                       placeholder="yyyy-MM-dd HH:mm:ss"
                                       value="$startLogTime"/>
                            </div>
                            <label class="control-label" style="width: 20px;float: left;">到</label>

                            <div class="col-sm-3">
                                <input class="form-control datepicker" type="text" style="width:160px"
                                       id="endLogTime"
                                       name="endLogTime"
                                       date-format="yyyy-MM-dd HH:mm:ss"
                                       placeholder="yyyy-MM-dd HH:mm:ss"
                                       value="$endLogTime"/>
                            </div>
                            <div class="col-sm-2">
                                <div class="col-sm-2">
                                    <button class="btn btn-primary" type="button" id="searchBtn">
                                        搜索
                                    </button>
                                </div>
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
            <th data-toggle="true">任务ID</th>
            <th>日志记录时间</th>
            <th data-hide="all">日志创建时间</th>
            <th>执行节点组</th>
            <th data-hide="all">执行节点标识</th>
            <th data-hide="phone,tablet">提交节点组</th>
            <th>日志类型</th>
            <th>执行结果</th>
            <th data-hide="all">日志级别</th>
            <th data-hide="all">重试次数</th>
            <th data-hide="all">优先级</th>
            <th data-hide="all">执行时间</th>
            <th>Cron表达式</th>
            <th data-hide="all">反馈客户端</th>
            <th data-hide="all">用户参数</th>
            <th data-hide="all">内容</th>
        </tr>
        </thead>
        <tbody>
        {{each rows as row index}}
        <tr>
            <td>{{row.taskId}}</td>
            <td>{{row.logTime | dateFormat:'yyyy-MM-dd HH:mm:ss'}}</td>
            <td>{{row.gmtCreated | dateFormat:'yyyy-MM-dd HH:mm:ss'}}</td>
            <td>{{row.taskTrackerNodeGroup}}</td>
            <td>{{row.taskTrackerIdentity}}</td>
            <td>{{row.submitNodeGroup}}</td>
            <td>{{row.logType | format:'logTypeLabel'}}</td>
            <td>{{row.success | format:'successLabel'}}</td>
            <td>{{row.level}}</td>
            <td>{{row.retryTimes}}</td>
            <td>{{row.priority}}</td>
            <td>{{row.triggerTime | dateFormat:'yyyy-MM-dd HH:mm:ss'}}</td>
            <td>{{row.cronExpression}}</td>
            <td>{{row.needFeedback | format:'needFeedbackLabel'}}</td>
            <td>{{row.extParams | format:'stringifyJSON'}}</td>
            <td>
                <pre>{{row.msg}}</pre>
            </td>
        </tr>
        {{/each}}
        {{if results == 0}}
        <tr>
            <td colspan="16">暂无数据</td>
        </tr>
        {{/if}}
        </tbody>
        <tfoot>
        <tr>
            <td colspan="8">
                <span>共{{results}}条记录，每页展示{{pageSize}}条</span>
                <ul class="pagination-sm pull-right"></ul>
            </td>
        </tr>
        </tfoot>
    </table>
</script>

<style>
    pre {
        padding: 0px;
        background-color: transparent;
        border: 0;
    }
</style>

<script>
    $(document).ready(function () {

        var LOG_TYPE = {
            RECEIVE: '接受任务',
            SENT: '派发任务',
            FINISHED: '完成任务',
            FIXED_DEAD: '修复死任务',
            BIZ: '业务日志',
            RESEND: '重新反馈任务',
            DEL: '删除'
        };

        LTS.colFormatter.logTypeLabel = function (v) {
            return v ? LOG_TYPE[v] : v;
        }
        LTS.colFormatter.successLabel = function (v) {
            return v ? "成功" : "失败";
        }

        var ltsTable = $("#ltstableContainer").ltsTable({
            url: 'api/job-logger/job-logger-get',
            templateId: 'ltstable'
        });

        $(document).on("click", "#searchBtn", function () {
            var taskId = $("input[name='taskId']").val();
//            if (!taskId) {
//                sweetAlert("请输入任务ID", "请输入你提交的任务ID(TaskId)", "error");
//                return;
//            }
            var taskTrackerNodeGroup = $("select[name='taskTrackerNodeGroup']").val();
            var startLogTime = $("input[name='startLogTime']").val();
            var endLogTime = $("input[name='endLogTime']").val();
            if (!startLogTime) {
                sweetAlert("请输入开始查询时间", "格式 yyyy-MM-dd HH:mm:ss", "error");
                return;
            }
            if (!LTS.ReExp.time.test(startLogTime)) {
                sweetAlert("开始查询时间格式不正确", "格式 yyyy-MM-dd HH:mm:ss", "error");
                return;
            }
            if (!endLogTime) {
                sweetAlert("请输入结束查询时间", "格式 yyyy-MM-dd HH:mm:ss", "error");
                return;
            }
            if (!LTS.ReExp.time.test(endLogTime)) {
                sweetAlert("结束查询时间格式不正确", "格式 yyyy-MM-dd HH:mm:ss", "error");
                return;
            }

            var params = {
                taskId: taskId,
                taskTrackerNodeGroup: taskTrackerNodeGroup,
                startLogTime: startLogTime, endLogTime: endLogTime
            };
            if (!taskId) {
                // 如果没有按ID查询，那么要对时间跨度长的做提醒(超过1小时的)
                if ((DateUtil.parse(endLogTime).getTime() - DateUtil.parse(startLogTime).getTime()) > 60 * 60 * 1000) {
                    swal({
                        title: "确认查询操作？",
                        text: "您当前查询日志没有按照任务ID查询，时间跨度大于1小时，可能查询结果会比较大 !",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        cancelButtonText: "取消",
                        confirmButtonText: "我要查询",
                        closeOnConfirm: true
                    }, function (isConfirm) {
                        if (isConfirm) {
                            ltsTable.post(params, 1);
                        }
                    });
                } else {
                    ltsTable.post(params, 1);
                }
            } else {
                ltsTable.post(params, 1);
            }

        });

        var taskId = $("input[name='taskId']").val();
        if (taskId) {
            // 如果是按ID查询，要把时间设置长一点
            var startLongTime = DateUtil.formatYMDHMD(DateUtil.getDate(new Date(), -10));
            $("input[name='startLogTime']").val(startLongTime);
            $("#searchBtn").trigger("click");
        } else {
            ltsTable.renderEmpty();
        }
    });
</script>