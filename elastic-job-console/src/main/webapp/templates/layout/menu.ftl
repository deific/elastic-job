<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="dropdown profile-element">
                    <div class="env-element" id="current-env" env="daily"><a href="./">DPC管理后台</a></div>
                </div>
                <div class="logo-element">DPC</div>
            </li>
            
            <li>
                <a href="#"><i class="fa fa-dashboard"></i> <span class="nav-label">任务概况</span></a>
            </li>
            <li>
                <a href="#"><i class="fa fa-server"></i> <span class="nav-label">任务管理</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="node-manager.htm">节点管理</a></li>
                    <li><a href="node-group-manager.htm">任务状态</a></li>
                    <li><a href="node-jvm-info.htm">注册中心</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-random"></i> <span class="nav-label">注册中心</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="job-add.htm">任务添加</a></li>
                    <li><a href="cron-job-queue.htm">Cron任务</a></li>
                    <li><a href="executing-job-queue.htm">执行中的任务</a></li>
                    <li><a href="executable-job-queue.htm">等待执行的任务</a></li>
                    <li><a href="load-job.htm">手动加载任务</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-file-text-o"></i> <span class="nav-label">日志</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="job-logger.htm">任务日志</a></li>
                    <li><a href="node-onoffline-log.htm">节点上下线日志</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-bar-chart"></i> <span class="nav-label">监控报警</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="jobtracker-monitor.htm">JobTracker监控</a></li>
                    <li><a href="tasktracker-monitor.htm">TaskTracker监控</a></li>
                    <li><a href="h2" target="_blank">H2-Console</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>