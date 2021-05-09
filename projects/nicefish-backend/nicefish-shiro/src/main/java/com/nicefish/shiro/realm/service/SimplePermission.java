package com.nicefish.shiro.realm.service;

import org.apache.shiro.authz.Permission;
import org.springframework.util.StringUtils;

/**
 * @author kimmking (kimmking@163.com)
 * @date 2013-12-5
 */
public class SimplePermission implements Permission {

	private String perm;

	public String getPerm() {
		return perm;
	}

	public void setPerm(String perm) {
		this.perm = perm;
	}

	public SimplePermission(String permissionString) {
		this.perm = permissionString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.authz.Permission#implies(org.apache.shiro.authz.Permission
	 * )
	 */
	@Override
	public boolean implies(Permission p) {
        SimplePermission sp = (SimplePermission) p;
        if (sp == null || StringUtils.isEmpty(sp.getPerm())) {
            return true;
        }
        if(this.getPerm().equals(sp.getPerm()) || this.getPerm().equals(sp.getPerm().replace(".","/"))){
            return true;
        }
        return false;
		}

	@Override
	public String toString() {
		return "SimplePermission[" + this.perm + "]";
	}

	public boolean isRest() {
		return Boolean.FALSE;
	}

}