package com.butenov.jobplatform.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRegistrationInfo extends UserRegistrationInfo
{
	private String resumeUrl;  // You can add additional fields for Candidate-specific details
}

