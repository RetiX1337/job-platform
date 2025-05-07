package com.butenov.jobplatform.candidates.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CandidateSearchCriteria
{
	//private String title = "";
	private String location = "";
	private Set<Long> skillIds = new HashSet<>();
}
