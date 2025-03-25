package com.butenov.jobplatform.matching.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobCandidateMatchDto
{
	@JsonProperty("experience_match")
	private final double experienceMatch;
	private final String justification;

}
