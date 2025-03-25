package com.butenov.jobplatform.matching.dto;

import java.util.List;

import com.butenov.jobplatform.candidates.model.Education;
import com.butenov.jobplatform.candidates.model.JobExperience;
import com.butenov.jobplatform.skills.model.Skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CandidateMatchingDto
{
	private List<JobExperience> jobExperiences;
	private List<Skill> skills;
	private List<Education> educations;
}
