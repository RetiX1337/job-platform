package com.butenov.jobplatform.jobs.repository;

import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.skills.model.Skill;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

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

	public static Specification<Job> sortByMatchingSkills(final Set<Long> candidateSkillIds)
	{
		return (root, query, criteriaBuilder) -> {
			// Check if the query result type is not Long (i.e. not a count query)
			if (!query.getResultType().equals(Long.class)) {
				final Join<Job, Skill> jobSkillsJoin = root.join("requiredSkills", JoinType.LEFT);
				Expression<Long> matchingSkillsCount = criteriaBuilder.count(jobSkillsJoin.get("id"));

				if (candidateSkillIds != null && !candidateSkillIds.isEmpty()) {
					Predicate matchingSkills = jobSkillsJoin.get("id").in(candidateSkillIds);
					query.where(criteriaBuilder.or(matchingSkills, jobSkillsJoin.get("id").isNull()));
				}

				query.groupBy(root.get("id"));
				query.orderBy(criteriaBuilder.desc(matchingSkillsCount));
			}

			return criteriaBuilder.conjunction();
		};
	}
}
