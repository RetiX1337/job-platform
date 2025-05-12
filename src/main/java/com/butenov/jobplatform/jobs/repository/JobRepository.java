package com.butenov.jobplatform.jobs.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.jobs.model.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>
{
	List<Job> findAllByCompany(Company company);

	@Query("SELECT j FROM Job j " +
			"WHERE j.company = :company " +
			"ORDER BY j.createdAt DESC")
	Page<Job> findByCompanyOrderByCreatedAtDesc(Company company, Pageable pageable);

	@Query(value = """
			SELECT j.* FROM Job j
			LEFT JOIN job_skills js
			  ON j.id = js.job_id
			 AND js.skill_id IN (:skillIds)
			WHERE (:title IS NULL OR lower(j.title) LIKE lower(concat('%',:title,'%')))
			  AND (:location IS NULL OR lower(j.location) LIKE lower(concat('%',:location,'%')))
			  AND (:minSalary IS NULL OR j.salary >= :minSalary)
			  AND (:maxSalary IS NULL OR j.salary <= :maxSalary)
			  AND ((:companyIds) IS NULL OR j.company_id IN (:companyIds))
			GROUP BY j.id
			ORDER BY COUNT(js.skill_id) DESC
			""",
			nativeQuery = true,
			countQuery = """
					SELECT count(DISTINCT j.id)
					FROM job j
					WHERE (:title IS NULL OR lower(j.title) LIKE lower(concat('%',:title,'%')))
					  AND (:location IS NULL OR lower(j.location) LIKE lower(concat('%',:location,'%')))
					  AND (:minSalary IS NULL OR j.salary >= :minSalary)
					  AND (:maxSalary IS NULL OR j.salary <= :maxSalary)
					  AND ((:companyIds) IS NULL OR j.company_id IN (:companyIds))
					"""
	)
	Page<Job> findBySkillsAndFiltersOrdered(
			@Param("skillIds") Set<Long> skillIds,
			@Param("title") String title,
			@Param("location") String location,
			@Param("minSalary") Double minSalary,
			@Param("maxSalary") Double maxSalary,
			@Param("companyIds") Set<Long> companyIds,
			Pageable pageable
	);

}
