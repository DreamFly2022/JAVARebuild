package com.nicefish.shiro.realm.service;

public class SimpleRestPermission extends SimplePermission {
	
	public static final String REST_PREFIX = "rest:";
	
	public SimpleRestPermission(String permissionString) {
		super(permissionString.replace(REST_PREFIX, ""));
	}

	@Override
	public String toString() {
		return "SimpleRestPermission[" + REST_PREFIX + this.getPerm() + "]";
	}

	public boolean isRest(){
		return Boolean.TRUE;
	}
}
