package com.myzh.dpc.console.job.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Robert HG (254963746@qq.com) on 6/6/15.
 */
@Controller
public class UIController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("node-manager")
    public String nodeManagerUI() {
        return "nodeManager";
    }

    @RequestMapping("node-group-manager")
    public String nodeGroupManagerUI() {
        return "nodeGroupManager";
    }

    @RequestMapping("node-onoffline-log")
    public String nodeOnOfflineLogUI(Model model) {
        model.addAttribute("startLogTime", "2015-01-01 00:00:12");
        model.addAttribute("endLogTime", "2015-01-01 00:00:12");
        return "nodeOnOfflineLog";
    }

    @RequestMapping("node-jvm-info")
    public String nodeJVMInfo(Model model, String identity) {
        model.addAttribute("identity", identity);
        return "nodeJvmInfo";
    }

    @RequestMapping("job-add")
    public String addJobUI(Model model) {
        setAttr(model);
        return "jobAdd";
    }

    @RequestMapping("job-logger")
    public String jobLoggerUI(Model model, String taskId, String taskTrackerNodeGroup,
                              Date startLogTime, Date endLogTime) {
        model.addAttribute("taskId", taskId);
        model.addAttribute("taskTrackerNodeGroup", taskTrackerNodeGroup);
        if (startLogTime == null) {
            startLogTime = new Date();
        }
        model.addAttribute("startLogTime", "2015-01-01");
        if (endLogTime == null) {
            endLogTime = new Date();
        }
        model.addAttribute("endLogTime", "2015-01-01");
        setAttr(model);
        return "jobLogger";
    }

    @RequestMapping("cron-job-queue")
    public String cronJobQueueUI(Model model) {
        setAttr(model);
        return "cronJobQueue";
    }

    @RequestMapping("executable-job-queue")
    public String executableJobQueueUI(Model model) {
        setAttr(model);
        return "executableJobQueue";
    }

    @RequestMapping("executing-job-queue")
    public String executingJobQueueUI(Model model) {
        setAttr(model);
        return "executingJobQueue";
    }

    @RequestMapping("load-job")
    public String loadJobUI(Model model) {
        setAttr(model);
        return "loadJob";
    }

    private void setAttr(Model model) {
        
//        model.addAttribute("jobClientNodeGroups", jobClientNodeGroups);
//        model.addAttribute("taskTrackerNodeGroups", taskTrackerNodeGroups);
    }

}
