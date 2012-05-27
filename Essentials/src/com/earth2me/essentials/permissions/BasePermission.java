package com.earth2me.essentials.permissions;

public class BasePermission extends AbstractSuperpermsPermission {
	protected String permission;

	public BasePermission(String base, String permission)
	{
		super();
		this.permission = base + permission;
	}

	public String getPermission()
	{
		return permission;
	}	
}
