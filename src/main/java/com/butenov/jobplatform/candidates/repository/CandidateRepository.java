package com.butenov.jobplatform.candidates.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.butenov.jobplatform.candidates.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate>
{
	@Query(value = """
			SELECT u.*, c.* 
			FROM users u
			JOIN candidate c ON c.id = u.id
			LEFT JOIN candidate_profile cp ON cp.candidate_id = c.id
			LEFT JOIN candidate_profile_skills cps ON cps.candidate_profile_id = cp.id
			    AND cps.skill_id IN (:skillIds)
			GROUP BY u.id, c.id
			ORDER BY COUNT(cps.skill_id) DESC
			""",
			countQuery = """
					    SELECT COUNT(DISTINCT u.id)
					    FROM users u
					    JOIN candidate c ON c.id = u.id
					    LEFT JOIN candidate_profile cp ON cp.candidate_id = c.id
					    LEFT JOIN candidate_profile_skills cps ON cps.candidate_profile_id = cp.id
					        AND cps.skill_id IN (:skillIds)
					""",
			nativeQuery = true
	)
	Page<Candidate> findByMatchingSkills(
			@Param("skillIds") Set<Long> skillIds,
			Pageable pageable
	);

}
