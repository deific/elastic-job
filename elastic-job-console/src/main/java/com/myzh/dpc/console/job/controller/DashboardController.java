
package com.myzh.dpc.console.job.controller;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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
    
    @RequiresPermissions(value = {"*"} )
    @RequestMapping(method = RequestMethod.GET)
    public String homepage(final ModelMap model) {
        RegistryCenterClient client = registryCenterService.connectActivated();
        if (!client.isConnected()) {
            return "redirect:/registry_center_page";
        }
        model.put(RegistryCenterController.CURATOR_CLIENT_KEY, client);
        return "redirect:main";
    }
    @RequestMapping(value = "logout")
    public String logout(final ModelMap model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    	return "redirect:index";  
    }
    
    @RequestMapping(value = "login")
    public String login(String userName, String password, final ModelMap model) {
    	UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
    	token.setRememberMe(true);  
        Subject subject = SecurityUtils.getSubject();
        try {
        	subject.login(token);
        	if (subject.isAuthenticated()) {  
                return "redirect:main";  
            } else {  
                return "login";  
            } 
        } catch (Exception e) {
        	e.printStackTrace();
        	String msg = "登录密码错误." + token.getPrincipal() + ""; 
        	model.addAttribute("message", msg);
        }
        return "login";  
    }
    
    @RequiresPermissions(value = {"*"} )
    @RequestMapping(value = "registry_center_page", method = RequestMethod.GET)
    public String registryCenterPage(final ModelMap model) {
        model.put("activedTab", 1);
        RegistryCenterClient client = registryCenterService.connectActivated();
        if (!client.isConnected()) {
            return "redirect:/registry_center_page";
        }
        model.put(RegistryCenterController.CURATOR_CLIENT_KEY, client);
        return "job/registry_center";
    }
    @RequiresPermissions(value = {"*"} )
    @RequestMapping(value = "job_detail", method = RequestMethod.GET)
    public String jobDetail(@RequestParam final String jobName, final ModelMap model) {
        model.put("jobName", jobName);
        return "job/job_detail";
    }
    @RequiresPermissions(value = {"*"} )
    @RequestMapping(value = "server_detail", method = RequestMethod.GET)
    public String serverDetail(@RequestParam final String serverIp, final ModelMap model) {
        model.put("serverIp", serverIp);
        return "job/server_detail";
    }
    
    @RequiresPermissions(value = {"*"} )
    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(final ModelMap model) {
        model.put("activedTab", 0);
        return "layout/main";
    }
    @RequiresPermissions(value = {"*"} )
    @RequestMapping(value = "help", method = RequestMethod.GET)
    public String help(final ModelMap model) {
        model.put("activedTab", 2);
        return "job/help";
    }
}
