package com.butenov.jobplatform.jobs.repository;

import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

import com.butenov.jobplatform.jobs.model.Job;

public class JobSpecifications
{
	public static Specification<Job> withFilters(final String title, final String location, final Double minSalary,
	                                             final Double maxSalary, final Set<Long> requiredSkillIds,
	                                             final Set<Long> companyIds)
	{
		return (root, query, criteriaBuilder) -> {
			Specification<Job> spec = Specification.where(null);

			if (title != null && !title.isBlank())
			{
				spec = spec.and((r, q, cb) -> cb.like(cb.lower(r.get("title")), "%" + title.toLowerCase() + "%"));
			}

			if (location != null && !location.isBlank())
			{
				spec = spec.and(
						(r, q, cb) -> cb.like(cb.lower(r.get("location")), "%" + location.toLowerCase() + "%"));
			}

			if (minSalary != null)
			{
				spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("salary"), minSalary));
			}

			if (maxSalary != null)
			{
				spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("salary"), maxSalary));
			}

			if (requiredSkillIds != null && !requiredSkillIds.isEmpty())
			{
				spec = spec.and((r, q, cb) -> r.join("requiredSkills").get("id").in(requiredSkillIds));
			}

			if (companyIds != null && !companyIds.isEmpty())
			{
				spec = spec.and((r, q, cb) -> r.get("company").get("id").in(companyIds));
			}

			return spec.toPredicate(root, query, criteriaBuilder);
		};
	}
}
