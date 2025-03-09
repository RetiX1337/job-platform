package com.butenov.jobplatform.jobs.converter;

import org.springframework.stereotype.Component;

import com.butenov.jobplatform.commons.Converter;
import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.companies.service.CompanyService;
import com.butenov.jobplatform.jobs.dto.JobForm;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.service.SkillService;

import java.util.stream.Collectors;

@Component
public class JobConverter implements Converter<Job, JobForm>
{

	private final CompanyService companyService;
	private final SkillService skillService;

	public JobConverter(final CompanyService companyService, final SkillService skillService)
	{
		this.companyService = companyService;
		this.skillService = skillService;
	}

	@Override
	public Job convertToEntity(final JobForm dto)
	{
		final Job job = new Job();
		job.setId(dto.getId());
		job.setTitle(dto.getTitle());
		job.setDescription(dto.getDescription());
		job.setLocation(dto.getLocation());
		job.setRequirements(dto.getRequirements());

		final Company company = companyService.findById(dto.getCompanyId());
		job.setCompany(company);

		if (dto.getRequiredSkillIds() != null)
		{
			job.setRequiredSkills(dto.getRequiredSkillIds().stream()
			                         .map(skillService::findById)
			                         .collect(Collectors.toSet()));
		}

		return job;
	}

	@Override
	public JobForm convertToDto(final Job entity)
	{
		final JobForm dto = new JobForm();
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setLocation(entity.getLocation());
		dto.setRequirements(entity.getRequirements());

		dto.setCompanyId(entity.getCompany().getId());

		if (entity.getRequiredSkills() != null)
		{
			dto.setRequiredSkillIds(entity.getRequiredSkills().stream()
			                              .map(Skill::getId)
			                              .collect(Collectors.toSet()));
		}

		return dto;
	}
}

