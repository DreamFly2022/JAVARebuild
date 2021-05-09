package com.nicefish.shiro.realm.service;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.springframework.beans.factory.annotation.Autowired;

public class SimplePermissionResolver implements PermissionResolver {

	@Override
	public Permission resolvePermission(String perm) {
		if (perm == null)
			return null;
		else if (perm.startsWith(SimpleRestPermission.REST_PREFIX))
			return new SimpleRestPermission(perm);
		else
			return new SimplePermission(perm);
	}

}
