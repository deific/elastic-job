
package com.myzh.dpc.console.job.controller;

import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.myzh.dpc.console.job.domain.RegistryCenterClient;
import com.myzh.dpc.console.job.domain.RegistryCenterConfiguration;
import com.myzh.dpc.console.job.service.RegistryCenterService;


@RestController
@RequestMapping("registry_center")
public class RegistryCenterController {
    
    public static final String CURATOR_CLIENT_KEY = "curator_client_key";
    	
    @Resource
    private RegistryCenterService registryCenterService;
    
    @RequestMapping(method = RequestMethod.GET)
    public Collection<RegistryCenterConfiguration> load(final HttpSession session) {
        setClientToSession(registryCenterService.connectActivated(), session);
        return registryCenterService.loadAll();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public boolean add(final RegistryCenterConfiguration config) {
        return registryCenterService.add(config);
    }
    
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void delete(final RegistryCenterConfiguration config) {
        registryCenterService.delete(config.getName());
    }
    
    @RequestMapping(value = "connect", method = RequestMethod.POST)
    public boolean connect(final RegistryCenterConfiguration config, final HttpSession session) {
        return setClientToSession(registryCenterService.connect(config.getName()), session);
    }
    
    private boolean setClientToSession(final RegistryCenterClient client, final HttpSession session) {
        boolean result = client.isConnected();
        if (result) {
            session.setAttribute(CURATOR_CLIENT_KEY, client);
        }
        return result;
    }
}
