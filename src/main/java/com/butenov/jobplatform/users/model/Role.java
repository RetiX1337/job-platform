package com.butenov.jobplatform.users.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority
{
	ROLE_USER, ROLE_ADMIN, ROLE_CANDIDATE, ROLE_RECRUITER;

	@Override
	public String getAuthority()
	{
		return name();
	}
}
