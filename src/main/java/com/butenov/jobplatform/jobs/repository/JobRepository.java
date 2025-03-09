package com.butenov.jobplatform.jobs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butenov.jobplatform.jobs.model.Job;

public interface JobRepository extends JpaRepository<Job, Long>
{
}
