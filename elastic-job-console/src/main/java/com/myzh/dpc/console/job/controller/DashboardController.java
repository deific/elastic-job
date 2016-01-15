
package com.myzh.dpc.console.job.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.myzh.dpc.console.job.domain.RegistryCenterClient;
import com.myzh.dpc.console.job.service.RegistryCenterService;

@Controller
@RequestMapping("/")
@SessionAttributes(RegistryCenterController.CURATOR_CLIENT_KEY)
public class DashboardController {
    
    @Resource
    private RegistryCenterService registryCenterService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String homepage(final ModelMap model) {
        RegistryCenterClient client = registryCenterService.connectActivated();
        if (!client.isConnected()) {
            return "redirect:registry_center_page";
        }
        model.put(RegistryCenterController.CURATOR_CLIENT_KEY, client);
        return "redirect:main";
    }
    
    @RequestMapping(value = "registry_center_page", method = RequestMethod.GET)
    public String registryCenterPage(final ModelMap model) {
        model.put("activedTab", 1);
        return "registry_center";
    }
    
    @RequestMapping(value = "job_detail", method = RequestMethod.GET)
    public String jobDetail(@RequestParam final String jobName, final ModelMap model) {
        model.put("jobName", jobName);
        return "job_detail";
    }
    
    @RequestMapping(value = "server_detail", method = RequestMethod.GET)
    public String serverDetail(@RequestParam final String serverIp, final ModelMap model) {
        model.put("serverIp", serverIp);
        return "server_detail";
    }
    
    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(final ModelMap model) {
        model.put("activedTab", 0);
        return "layout/main";
    }
    
    @RequestMapping(value = "help", method = RequestMethod.GET)
    public String help(final ModelMap model) {
        model.put("activedTab", 2);
        return "help";
    }
}
