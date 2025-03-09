package com.butenov.jobplatform.jobs.dto;

import lombok.Data;
import java.util.Set;

@Data
public class JobForm {

	private Long id;
	private String title;
	private String description;
	private String location;
	private String requirements;
	private Long companyId;
	private Set<Long> requiredSkillIds;

}

