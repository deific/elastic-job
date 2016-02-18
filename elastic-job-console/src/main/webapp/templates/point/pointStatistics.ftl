<#assign base=springMacroRequestContext.getContextUrl("")> 
<script type="text/javascript" src="${base}/assets/Highcharts-4.2.1/js/highcharts.js"></script>
<div class="row wrapper border-bottom white-bg page-heading" >
    <div class="col-lg-12">
        <h2>积分统计</h2>
        <ol class="breadcrumb">
            <li><a>积分</a></li>
            <li><a href="#">积分统计</a></li>
        </ol>
    </div>
</div>
<div class="wrapper wrapper-content animated fadeInRight">
	<form id="queryForm" class="form-horizontal" action="doStatistics">
		<div class="form-group">
			<div class="col-lg-8 btn-group">
				  <a href="javascript:showStatistics(7)" class="btn btn-default">7天内</a>
				  <a href="javascript:showStatistics(30)" class="btn btn-default">30天内</a>
				  <a href="javascript:showStatistics(365)" class="btn btn-default">1年内</a>
			</div>
			<div class="col-lg-3">
			    <div class="input-group">
			      <div class="input-group-addon"><i class="fa fa-plus-square"></i></div>
			      <input type="text" class="form-control" name="clinicCode" id="clinicCode" value="${clinicCode}" placeholder="请输入诊所编码">
			    </div>
	  		</div>
	  		<div class="col-lg-1">
			    <button type="button" class="btn btn-primary" onclick="showStatistics(null)">查询</button>
	  		</div>
		</div>
	</form>
	<div class="form-group">
    <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
    </div>
</div>

<script>
    $(document).ready(function () {
		showStatistics(7);
    });

    function showStatistics(days) {
    	if (days == null) {
    		days = 7;
    	}
		var data = {};
    	data.days=days;
    	data.clinicCode=$("#clinicCode").val();
    	$.getJSON('doStatistics', data, function (data) {
    		if (data.rtnCode == 0) {
    			highcharts(data.data.categories, data.data.series);
    			var chart = $('#container').highcharts();
	    		chart.setTitle(null,  { text: days + "天内积分变化曲线" });
    		}
    	});
    }
    function highcharts(categories,series) {
	    $('#container').highcharts({
	        chart: {
	        	events: {
	        		drilldown: function (e) {
	        			alert("as");
	        		}
	        	}
	        },
	        title: {
	            text: '积分趋势变化图',
	            x: -20 //center
	        },
	        subtitle: {
	            text: '积分变化曲线',
	            x: -20
	        },
	        xAxis: {
	            categories: categories
	        },
	        yAxis: {
	            title: {
	                text: '积分'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },
	        series: series
	    });
	   
    }
</script>