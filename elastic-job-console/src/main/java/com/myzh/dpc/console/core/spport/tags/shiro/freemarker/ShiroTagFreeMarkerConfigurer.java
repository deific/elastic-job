package com.myzh.dpc.console.core.spport.tags.shiro.freemarker;

import java.io.IOException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import freemarker.template.TemplateException;

public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {
	@Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("authenticated", new AuthenticatedTag());
        this.getConfiguration().setSharedVariable("guest", new GuestTag());
        this.getConfiguration().setSharedVariable("hasAnyRoles", new HasAnyRolesTag());
        this.getConfiguration().setSharedVariable("hasPermission", new HasPermissionTag());
        this.getConfiguration().setSharedVariable("hasRole", new HasRoleTag());
        this.getConfiguration().setSharedVariable("lacksPermission", new LacksPermissionTag());
        this.getConfiguration().setSharedVariable("lacksRole", new LacksRoleTag());
        this.getConfiguration().setSharedVariable("notAuthenticated", new NotAuthenticatedTag());
        this.getConfiguration().setSharedVariable("principal", new PrincipalTag());
        this.getConfiguration().setSharedVariable("user", new UserTag());
//        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }
}
