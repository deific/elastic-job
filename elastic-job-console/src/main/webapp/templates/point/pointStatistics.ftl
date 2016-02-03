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
	<div class="form-group">
		<div class="btn-group">
		  <a href="javascript:showStatistics(7)" class="btn btn-default">7天内</a>
		  <a href="javascript:showStatistics(30)" class="btn btn-default">30天内</a>
		  <a href="javascript:showStatistics(365)" class="btn btn-default">1年内</a>
		</div>
		<div class="col-lg-8">
			<form class="form-inline">
  <div class="form-group">
    <label class="sr-only" for="exampleInputAmount">Amount (in dollars)</label>
    <div class="input-group">
      <div class="input-group-addon"><i class="fa fa-plus-square"></i></div>
      <input type="text" class="form-control" id="clinicCode" placeholder="请输入诊所编码">
    </div>
  </div>
  <button type="submit" class="btn btn-primary">查询</button>
</form>
  		</div>
	</div>
    <div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</div>

<script>
    $(document).ready(function () {
		showStatistics(7);
    });

    function showStatistics(days) {
    	$.getJSON('doStatistics?days=' + days, function (data) {
    		if (data.rtnCode == 0) {
    			highcharts(data.data.categories, data.data.series);
    			var chart = $('#container').highcharts();
	    		chart.setTitle(null,  { text: days + "天内分省积分变化曲线" });
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
	            text: '积分',
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