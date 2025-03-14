package com.butenov.jobplatform.security.service.impl;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.security.dto.CandidateRegistrationInfo;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.users.model.Role;
import com.butenov.jobplatform.users.service.UserService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CandidateRegistrationService
{
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public CandidateRegistrationService(final UserService userService, final PasswordEncoder passwordEncoder)
	{
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	public void register(final CandidateRegistrationInfo candidateRegistrationInfo)
	{
		final String encodedPassword = passwordEncoder.encode(candidateRegistrationInfo.getPassword());

		final Candidate candidate = new Candidate();
		candidate.setFirstName(candidateRegistrationInfo.getFirstName());
		candidate.setLastName(candidateRegistrationInfo.getLastName());
		candidate.setEmail(candidateRegistrationInfo.getEmail());
		candidate.setPassword(encodedPassword);
		candidate.setRoles(Set.of(Role.ROLE_CANDIDATE));

		userService.save(candidate);
	}
}
