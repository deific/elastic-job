package com.myzh.dpc.console.sys.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class SecurityRealm extends AuthorizingRealm {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//获取当前登录的用户名,等价于(String)principals.fromRealm(this.getName()).iterator().next()  
        String currentUsername = (String)super.getAvailablePrincipal(principals);
		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
		//实际中可能会像上面注释的那样从数据库取得  
        if(null!=currentUsername && "root".equals(currentUsername)){  
            //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色    
            simpleAuthorInfo.addRole("admin");  
            //添加权限  
            simpleAuthorInfo.addStringPermission("admin:manage");  
            System.out.println("已为用户[mike]赋予了[admin]角色和[admin:manage]权限");  
            return simpleAuthorInfo;  
        }
        //详见applicationContext.xml中的<bean id="shiroFilter">的配置  
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
		if("root".equals(token.getUsername())){  
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo("root", "root", this.getName());  
            return authcInfo;  
        }
		return null;
	}
}
