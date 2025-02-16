package com.butenov.jobplatform.users.services;

import com.butenov.jobplatform.users.User;

public interface UserService
{
	User findByEmail(String email);

	User save(User user);
}
