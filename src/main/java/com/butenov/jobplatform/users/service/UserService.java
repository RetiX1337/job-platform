package com.butenov.jobplatform.users.service;

import com.butenov.jobplatform.users.model.User;

public interface UserService
{
	User findByEmail(String email);

	User save(User user);
}
