package com.butenov.jobplatform.jobapplications.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butenov.jobplatform.jobapplications.model.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>
{
}
