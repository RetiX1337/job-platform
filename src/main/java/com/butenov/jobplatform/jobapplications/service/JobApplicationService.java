package com.butenov.jobplatform.jobapplications.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.recruiters.model.Recruiter;

public interface JobApplicationService
{
	void save(final JobApplication jobApplication);

	void applyForJob(final Job job, final Candidate candidate);

	JobApplication findById(final Long id);

	List<JobApplication> findAll();

	List<JobApplication> findByJobId(final Long jobId);

	void acceptApplication(final JobApplication jobApplication);

	void rejectApplication(final JobApplication jobApplication);

	List<JobApplication> findByCandidateId(final Long candidateId);

	void deleteById(final Long id);

	boolean candidateHasAppliedForJob(final Job job, final Candidate candidate);

	Page<JobApplication> findLatestApplicationsForRecruiter(Recruiter recruiter, Pageable pageable);
}
