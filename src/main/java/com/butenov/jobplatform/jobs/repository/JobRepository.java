package com.butenov.jobplatform.jobs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.jobs.model.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>
{
	List<Job> findAllByCompany(Company company);

	@Query("SELECT j FROM Job j " +
			"WHERE j.company = :company " +
			"ORDER BY j.createdAt DESC")
	Page<Job> findByCompanyOrderByCreatedAtDesc(Company company, Pageable pageable);
}
