package com.butenov.jobplatform.candidates.dto;

import java.util.List;

import com.butenov.jobplatform.candidates.model.Education;
import com.butenov.jobplatform.candidates.model.JobExperience;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfileEditingDto
{
	private String cvLink;
	private String firstName;
	private String lastName;
	private List<JobExperience> jobExperienceList;
	private List<Education> educationList;
	private List<Long> skillIds;
}
