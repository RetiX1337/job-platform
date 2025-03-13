package com.butenov.jobplatform.jobs.dto;

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
public class JobSearchCriteria
{
	private String title = "";
	private String location = "";
	private Double minSalary;
	private Double maxSalary;
	private Set<Long> companyIds = new HashSet<>();
	private Set<Long> requiredSkillIds = new HashSet<>();
}
