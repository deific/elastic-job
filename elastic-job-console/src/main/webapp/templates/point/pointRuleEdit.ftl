<#assign base=springMacroRequestContext.getContextUrl("")> 
<link rel="stylesheet" href="${base}/assets/bootstrapvalidator-0.5.3/css/bootstrapValidator.min.css"/>
<script type="text/javascript" src="${base}/assets/bootstrapvalidator-0.5.3/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${base}/assets/bootstrapvalidator-0.5.3/js/language/zh_CN.js"></script>
<div class="row wrapper border-bottom white-bg page-heading" >
    <div class="col-lg-12">
        <h2>规则编辑</h2>
        <ol class="breadcrumb">
            <li><a>积分</a></li>
            <li><a href="${base}/rule">积分规则</a></li>
            <li class="active"><b>规则编辑</b></li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <form id="ruleForm" method="post" action="${base}/rule/save" class="form-horizontal" >
                    	<input type="hidden" name="id" value="${(rule.id)!}">
                    	<fieldset>
                        	<legend><h4>规则定义</h4></legend>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">名称</label>
	                            <div class="col-sm-4">
	                                <input type="text" class="form-control" name="name" value="${(rule.name)!}"
	                                       placeholder="请输入规则名称【必填】">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">描述</label>
	                            <div class="col-sm-8">
	                                <input type="text" class="form-control" name="desc" value="${(rule.desc)!}"
	                                       placeholder="请输入描述【必填】">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                        	<label class="col-sm-2 control-label">行为编码</label>
	                            <div class="col-sm-4">
	                            	<select name="behaviorCode" class="form-control">
	                            		<option value="00101001" <#if (rule.behaviorCode)?? && rule.behaviorCode=="00101001" >selected</#if>>00101001 诊疗软件-登录</option>
	                                    <option value="00102001" <#if (rule.behaviorCode)?? && rule.behaviorCode=="00102001" >selected</#if>>00102001 诊疗软件-连续登录</option>
	                                    <option value="00103004" <#if (rule.behaviorCode)?? && rule.behaviorCode=="00103004" >selected</#if>>00103004 诊疗软件-在线时间</option>
	                                    <option value="00101002" <#if (rule.behaviorCode)?? && rule.behaviorCode=="00101002" >selected</#if>>00101002 诊疗软件-开处方</option>
	                                    <option value="00201001" <#if (rule.behaviorCode)?? && rule.behaviorCode=="00201001" >selected</#if>>00201001 明医在线-登录</option>
	                                    <option value="00201002" <#if (rule.behaviorCode)?? && rule.behaviorCode=="00201002" >selected</#if>>00201002 明医在线-页面点击</option>
	                                </select>
	                            </div>
	                        </div>
                        </fieldset>
                        <fieldset>
                        	<legend><h4>规则条件</h4></legend>
	                        <div class="form-group">
	                        	<label class="col-sm-2 control-label">区域</label>
	                            <div class="col-sm-8">
	                                <input type="text" class="form-control" name="areaCode" value="${(rule.areaCode)!}"
	                                       placeholder="请输区域编码，省编码为2位，市编码为4位，县编码为6位">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">诊所</label>
	                            <div class="col-sm-3">
	                                <input type="text" class="form-control" name="clinicCode" value="${(rule.clinicCode)!}"
	                                       placeholder="请输入诊所编码">
	                            </div>
	                            <label class="col-sm-2 control-label">代理商</label>
	                            <div class="col-sm-3">
	                                <input type="text" class="form-control" name="agentCode" value="${(rule.agentCode)!}"
	                                       placeholder="请输入代理商编码">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">用户类别</label>
	                            <div class="col-sm-3">
	                            	<select name="userClass" class="form-control">
	                            		<option value="" <#if (rule.userClass)?? && rule.userClass=="" >selected</#if>>不限</option>
	                                    <option value="vip" <#if (rule.userClass)?? && rule.userClass=="vip" >selected</#if>>VIP</option>
	                                    <option value="normal" <#if (rule.userClass)?? && rule.userClass=="normal" >selected</#if>>普通</option>
	                                </select>
	                            </div>
	                            <label class="col-sm-2 control-label">用户类型</label>
	                            <div class="col-sm-3">
	                            	<select name="userType" class="form-control">
	                            		<option value="" <#if (rule.userType)?? && rule.userType=="" >selected</#if>>不限</option>
	                                    <option value="superAdmin" <#if (rule.userType)?? && rule.userType=="superAdmin" >selected</#if>>superAdmin</option>
	                                </select>
	                            </div>
	                        </div>
                        </fieldset>
                        <fieldset>
                        	<legend><h4>规则内容</h4></legend>
	                        
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label" title="行为的计算方式，如计算行为发生的天数，次数，行为产生业务值的累计">计算模式</label>
	                            <div class="col-sm-3">
	                                <select name="ruleMode" class="form-control" >
	                                    <option value="days" <#if (rule.ruleMode)?? && rule.ruleMode=="days" >selected</#if>>计算天数</option>
	                                    <option value="times" <#if (rule.ruleMode)?? && rule.ruleMode=="times" >selected</#if>>计算次数</option>
	                                    <option value="total" <#if (rule.ruleMode)?? && rule.ruleMode=="total" >selected</#if>>计算累计</option>
	                                </select>
	                            </div>
	                            <label class="col-sm-2 control-label">计算周期</label>
	                            <div class="col-sm-3">
	                                <select name="scope" class="form-control" title="行为的计算周期，如按天计算时，同一天内，算一次">
	                                	<option value="none" <#if (rule.scope)?? && rule.scope=="none" >selected</#if>>不限</option>
	                                    <option value="day" <#if (rule.scope)?? && rule.scope=="day" >selected</#if>>每天</option>
	                                    <option value="everyTime" <#if (rule.scope)?? && rule.scope=="onece" >selected</#if>>每次</option>
	                                </select>
	                            </div>
	                            
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">规则表达式</label>
	                            <div class="col-sm-3">
	                                <input type="text" class="form-control" name="ruleExpression" value="${(rule.ruleExpression)!}"
	                                       placeholder="请输入规则表达式，如：100-200,200">
	                            </div>
	                            <label class="col-sm-2 control-label">规则值</label>
	                            <div class="col-sm-4" >
	                                <input type="text" class="form-control" name="value" value="${(rule.value)!}"
	                                       placeholder="请输入规则值，如：10,5">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">周期最大值</label>
	                            <div class="col-sm-3" >
	                                <input type="number" class="form-control" name="maxValue" value="${(rule.maxValue)!}"
	                                       placeholder="请输入周期最大值，如：10">
	                            </div>
	                            <label class="col-sm-2 control-label">规则消息</label>
	                            <div class="col-sm-4" >
	                                <input type="text" class="form-control" name="ruleMessage" value="${(rule.ruleMessage)!}"
	                                       placeholder="请输入规则消息，如：奖励#value积分">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">优先级</label>
	                            <div class="col-sm-3">
	                                <input type="number" class="form-control" name="priority" value="${(rule.agentCode)!10}"
	                                       placeholder="必须为数字，数值越小，优先级越大【必填】">
	                            </div>
	                            <label class="col-sm-2 control-label">是否排他</label>
	                            <div class="col-sm-3">
	                                <div class="switch switch-small">
							            <label>
							               <input type="checkbox" value="1" name="exclusive" <#if (rule.exclusive)?? && rule.exclusive > checked </#if>> 排他时，其他同类型规则不执行
							            </label>
							         </div>
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">生效时间</label>
	                            <div class="col-sm-3">
	                                <input class="form-control datepicker" type="text" value="${((rule.effectiveTime)?string('yyyy-MM-dd HH:mm:ss'))!}"
	                                       id="effectiveTime"
	                                       name="effectiveTime"
	                                       date-format="yyyy-MM-dd HH:mm:ss"
	                                       placeholder="请输入触发时间"/>
	                            </div>
	                            <label class="col-sm-2 control-label">过期时间</label>
	                            <div class="col-sm-3" >
	                                <input class="form-control datepicker" type="text" value="${((rule.expireTime)?string('yyyy-MM-dd HH:mm:ss'))!}"
	                                       id="expireTime"
	                                       name="expireTime"
	                                       date-format="yyyy-MM-dd HH:mm:ss"
	                                       placeholder="请输入触发时间"/>
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">是否启用</label>
								<div class="col-sm-3" >
	                                <select name="state" class="form-control">
	                                    <option value="1" <#if (rule.state)?? && rule.state=="1" >selected</#if>>是</option>
	                                    <option value="0" <#if (rule.state)?? && rule.state=="0" >selected</#if>>否</option>
	                                </select>
	                            </div>
	                        </div>
                        </fieldset>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-1 col-sm-offset-2" >
                                <button type="submit" class="btn btn-primary">保存</button>
                            </div>
                            <div class="col-sm-1">
                                <button class="btn btn-warning" type="reset" id="resetBtn">
                                    		重置
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
		validForm();
        
    });
    
    function saveRule() {
    	$('#ruleForm').bootstrapValidator();
    	
    }
    function validForm() {
	    $('#ruleForm').bootstrapValidator({
	        feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
	        fields: {
	            name: {
	                validators: {
	                    notEmpty: {}
	                }
	            },
	            desc: {
	                validators: {
	                    notEmpty: {}
	                }
	            }
	        }
	    }).on('success.form.bv', function (e) {
				e.preventDefault();
				var form = $('#ruleForm');
				$.post(form.attr('action'), form.serialize(), function(result) {
					if (0 == result.rtnCode) {
						swal({title:"规则保存成功！", type:"success"},function(isConfirm){
							location.href="${base}/rule";
						});
					} else {
						sweetAlert("保存失败", result.rtnMessage, "error");
					}
			    }, 'json');
							
			});;
    }
</script>