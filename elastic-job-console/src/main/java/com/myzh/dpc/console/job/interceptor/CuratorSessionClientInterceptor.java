
package com.myzh.dpc.console.job.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.myzh.dpc.console.job.controller.RegistryCenterController;
import com.myzh.dpc.console.job.domain.RegistryCenterClient;
import com.myzh.dpc.console.job.util.SessionCuratorClient;

public final class CuratorSessionClientInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        RegistryCenterClient client = (RegistryCenterClient) request.getSession().getAttribute(RegistryCenterController.CURATOR_CLIENT_KEY);
        if (null == client || !client.isConnected()) {
            return false;
        }
        SessionCuratorClient.setCuratorClient(client.getCuratorClient());
        return true;
    }
    
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        SessionCuratorClient.clear();
    }
}
