package com.butenov.jobplatform.users.security.services;

import com.butenov.jobplatform.users.User;
import com.butenov.jobplatform.users.security.dto.UserRegistrationInfo;

public interface RegistrationService
{
	User register(final UserRegistrationInfo userRegistrationInfo);
}
