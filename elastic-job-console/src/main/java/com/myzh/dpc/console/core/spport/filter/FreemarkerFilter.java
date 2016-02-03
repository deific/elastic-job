package com.myzh.dpc.console.core.spport.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.myzh.dpc.console.utils.SpringContextUtil;

public class FreemarkerFilter implements Filter {
	private Locale locale;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String localeStr = filterConfig.getInitParameter("locale");
        if(StringUtils.hasText(localeStr)){
            locale = new Locale(localeStr);
        }else {
            locale = Locale.getDefault();
        }
	}
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        try {
            String name = req.getRequestURI();
            name = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."));
            FreeMarkerViewResolver viewResolver = (FreeMarkerViewResolver)SpringContextUtil.getBean(FreeMarkerViewResolver.class);;
            System.out.println("zzz:"+name);
            System.out.println("zzz:"+locale);
            View view = viewResolver.resolveViewName("decorators/" + name, locale);
            view.render(null, req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
	}
}
