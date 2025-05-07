package com.butenov.jobplatform.candidates.repository;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.butenov.jobplatform.candidates.dto.CandidateSearchCriteria;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.model.CandidateProfile;
import com.butenov.jobplatform.skills.model.Skill;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class CandidateSpecifications
{

	public static Specification<Candidate> withFilters(final CandidateSearchCriteria criteria)
	{
		return (root, query, cb) -> {
			Specification<Candidate> spec = Specification.where(null);

			final Join<Object, Object> profileJoin = root.join("candidateProfile", JoinType.LEFT);

			if (criteria.getSkillIds() != null && !criteria.getSkillIds().isEmpty())
			{
				final Join<Object, Object> skillsJoin = profileJoin.join("skills", JoinType.LEFT);
				spec = spec.and((r, q, c) -> skillsJoin.get("id").in(criteria.getSkillIds()));
			}

			//if (criteria.getLocation() != null && !criteria.getLocation().isBlank())
			//{
			//	spec = spec.and((r, q, c) ->
			//			c.like(c.lower(r.get("location")), "%" + criteria.getLocation().toLowerCase() + "%"));
			//}

			return spec.toPredicate(root, query, cb);
		};
	}

	public static Specification<Candidate> sortByMatchingSkills(final Set<Long> jobRequiredSkillIds)
	{
		return (root, query, cb) -> {
			if (!query.getResultType().equals(Long.class))
			{
				final Join<Candidate, CandidateProfile> profileJoin = root.join("candidateProfile", JoinType.LEFT);
				final Join<CandidateProfile, Skill> skillJoin = profileJoin.join("skills", JoinType.LEFT);

				if (jobRequiredSkillIds != null && !jobRequiredSkillIds.isEmpty())
				{
					final Predicate matchingSkills = skillJoin.get("id").in(jobRequiredSkillIds);
					query.where(cb.or(matchingSkills, skillJoin.get("id").isNull()));
				}

				query.groupBy(root);
				query.orderBy(cb.desc(cb.count(skillJoin.get("id"))));
			}

			return cb.conjunction();
		};
	}
}
