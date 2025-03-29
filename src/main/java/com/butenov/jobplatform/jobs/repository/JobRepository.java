package com.butenov.jobplatform.jobs.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.skills.model.Skill;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>
{
	// TODO implement sorting by amount of corresponding skills
	@Query("""
			SELECT j
			FROM Job j
			LEFT JOIN j.requiredSkills s
			GROUP BY j
			ORDER BY
			    COUNT(s) DESC,
			    CASE WHEN COUNT(s) = 0 THEN 1 ELSE 0 END,
			    j.id
			""")
	Page<Job> findRelevantJobs(@Param("candidateSkills") Set<Skill> candidateSkills, Specification<Job> specification,
	                           Pageable pageable);
}
