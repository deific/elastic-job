<#assign base=springMacroRequestContext.getContextUrl("")> 
<link rel="stylesheet" href="${base}/assets/bootstrapvalidator-0.5.3/css/bootstrapValidator.min.css"/>
<script type="text/javascript" src="${base}/assets/bootstrapvalidator-0.5.3/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${base}/assets/bootstrapvalidator-0.5.3/js/language/zh_CN.js"></script>
<div class="row wrapper border-bottom white-bg page-heading" >
    <div class="col-lg-12">
        <h2>积分维护</h2>
        <ol class="breadcrumb">
            <li><a>积分</a></li>
            <li><a href="${base}/point">积分管理</a></li>
            <li class="active"><b>积分维护</b></li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox">
                <div class="ibox-content">
                    <form id="pointForm" method="post" action="${base}/point/save" class="form-horizontal" >
                    	<fieldset>
                        	<legend><h4>积分信息</h4></legend>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">用户ID</label>
	                            <div class="col-sm-3">
	                                <input type="text" class="form-control" name="userId" value=""
	                                       placeholder="请输入用户ID【必填】">
	                            </div>
	                            <label class="col-sm-2 control-label">诊所编码</label>
	                            <div class="col-sm-3">
	                                <input type="text" class="form-control" name="clinicCode" value=""
	                                       placeholder="请输入诊所编码【必填】">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                        	<label class="col-sm-2 control-label">积分类型</label>
	                            <div class="col-sm-3">
	                            	<select name="pointType" class="form-control">
	                            		<option value="0">产生</option>
	                                    <option value="1">消费</option>
	                                </select>
	                            </div>
	                        	<label class="col-sm-2 control-label">积分维度</label>
	                            <div class="col-sm-3">
	                            	<select name="pointDimension" class="form-control">
	                            		<option value="001">诊疗软件</option>
	                                    <option value="002">学术园地</option>
	                                    <option value="003">医疗直通车</option>
	                                    <option value="004">明医商城</option>
	                                    <option value="005">积分商城</option>
	                                    <option value="006">微信</option>
	                                    <option value="007">媒体</option>
	                                    <option value="008">金融</option>
	                                </select>
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">积分</label>
	                            <div class="col-sm-3">
	                                <input type="number" class="form-control" name="point" value=""
	                                       placeholder="请输入积分【必填】">
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <label class="col-sm-2 control-label">积分说明</label>
	                            <div class="col-sm-8">
	                                <input type="text" class="form-control" name="desc" value=""
	                                       placeholder="请输入积分说明【必填】">
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
    
    function validForm() {
	    $('#pointForm').bootstrapValidator({
	        feedbackIcons: {
	            valid: 'glyphicon glyphicon-ok',
	            invalid: 'glyphicon glyphicon-remove',
	            validating: 'glyphicon glyphicon-refresh'
	        },
	        fields: {
	            userId: {
	                validators: {
	                    notEmpty: {}
	                }
	            },
	            clinicCode: {
	                validators: {
	                    notEmpty: {}
	                }
	            },
	            point: {
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
				var form = $('#pointForm');
				$.post(form.attr('action'), form.serialize(), function(result) {
					if (0 == result.rtnCode) {
						swal({title:"积分保存成功！", type:"success"},function(isConfirm){
							location.href="${base}/point";
						});
					} else {
						sweetAlert("保存失败", result.rtnMessage, "error");
					}
			    }, 'json');
							
			});;
    }
</script>