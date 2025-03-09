package com.butenov.jobplatform.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.users.repository.UserRepository;

@Service
public class DefaultUserDetailsService implements UserDetailsService
{
	private final UserRepository userRepository;

	public DefaultUserDetailsService(final UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{
		return userRepository.findByEmail(username)
		                     .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}
