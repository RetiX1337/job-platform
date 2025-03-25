package com.butenov.jobplatform.matching.dto;

import java.util.List;

import com.butenov.jobplatform.skills.model.Skill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JobMatchingDto
{
	private String title;
	private String description;
	private String location;
	private String requirements;
	private List<Skill> requiredSkills;
}
