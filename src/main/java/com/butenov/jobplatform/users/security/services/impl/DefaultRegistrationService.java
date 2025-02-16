package com.butenov.jobplatform.users.security.services.impl;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.users.Role;
import com.butenov.jobplatform.users.User;
import com.butenov.jobplatform.users.security.dto.UserRegistrationInfo;
import com.butenov.jobplatform.users.security.services.RegistrationService;
import com.butenov.jobplatform.users.services.UserService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class DefaultRegistrationService implements RegistrationService
{
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public DefaultRegistrationService(final UserService userService, final PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	public User register(final UserRegistrationInfo userRegistrationInfo) {
		final String encodedPassword = passwordEncoder.encode(userRegistrationInfo.getPassword());

		final User user = User.builder()
		                      .firstName(userRegistrationInfo.getFirstName())
		                      .lastName(userRegistrationInfo.getLastName())
		                      .email(userRegistrationInfo.getEmail())
		                      .password(encodedPassword)
		                      .roles(Set.of(Role.ROLE_USER))
		                      .build();

		return userService.save(user);
	}
}
