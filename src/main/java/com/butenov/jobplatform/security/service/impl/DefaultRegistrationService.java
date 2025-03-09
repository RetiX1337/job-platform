package com.butenov.jobplatform.security.service.impl;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.security.dto.UserRegistrationInfo;
import com.butenov.jobplatform.users.model.Role;
import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.service.UserService;

import jakarta.transaction.Transactional;

//@Transactional
//@Service
//public class DefaultRegistrationService implements RegistrationService
//{
//	private final UserService userService;
//	private final PasswordEncoder passwordEncoder;
//
//	public DefaultRegistrationService(final UserService userService, final PasswordEncoder passwordEncoder) {
//		this.userService = userService;
//		this.passwordEncoder = passwordEncoder;
//	}
//
//	public User register(final UserRegistrationInfo userRegistrationInfo) {
//		final String encodedPassword = passwordEncoder.encode(userRegistrationInfo.getPassword());
//
//		final User user = User.builder()
//		                      .firstName(userRegistrationInfo.getFirstName())
//		                      .lastName(userRegistrationInfo.getLastName())
//		                      .email(userRegistrationInfo.getEmail())
//		                      .password(encodedPassword)
//		                      .roles(Set.of(Role.ROLE_USER))
//		                      .build();
//
//		return userService.save(user);
//	}
//}
