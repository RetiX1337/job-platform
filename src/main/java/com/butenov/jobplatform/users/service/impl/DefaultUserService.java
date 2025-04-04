package com.butenov.jobplatform.users.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.repository.UserRepository;
import com.butenov.jobplatform.users.service.UserService;

@Service
public class DefaultUserService implements UserService
{
	private final UserRepository userRepository;

	public DefaultUserService(final UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@Override
	public User findByEmail(final String email)
	{
		return userRepository.findByEmail(email)
		                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public User save(final User user)
	{
		userRepository.findByEmail(user.getEmail())
		              .ifPresent(existingUser -> {
			              throw new IllegalArgumentException("User already exists: " + existingUser.getUsername());
		              });

		return userRepository.save(user);
	}
}
