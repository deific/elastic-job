<#assign base=springMacroRequestContext.getContextUrl("")>

<!DOCTYPE html>
<html>
<head>
    <#include "../templates/layout/head.ftl">
    <#import "../templates/tags/dashboard.ftl" as dashboard>
</head>

<body class="pace-done">
<div id="wrapper">
    <#include "../templates/layout/menu.ftl">
	
    <div id="page-wrapper" class="gray-bg" style="">
     	<#include "../templates/layout/topbar.ftl">
        <sitemesh:write property='body'/>
        <#include "../templates/layout/footer.ftl">
    </div>
</div>

<@dashboard.successDialog "success-dialog" />
<@dashboard.failureDialog "connect-reg-center-failure-dialog" "连接失败，请检查注册中心配置" />
<script src="${base}/js/common.js"></script>
<script src="${base}/js/dashboard.js"></script>
<script src="${base}/js/overview.js"></script>
</body>
</html>
