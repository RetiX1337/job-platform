package com.butenov.jobplatform.candidates.dto;

import java.util.List;

import com.butenov.jobplatform.candidates.model.Education;
import com.butenov.jobplatform.candidates.model.JobExperience;

import lombok.Data;

@Data
public class LlmCvProcessingDto
{
	private List<JobExperience> jobExperiences;
	private List<Education> educations;
	private List<String> skills;
}
