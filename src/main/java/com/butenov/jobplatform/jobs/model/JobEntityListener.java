package com.butenov.jobplatform.jobs.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PreUpdate;

public class JobEntityListener
{
	@PersistenceContext
	private EntityManager entityManager;

	@PreUpdate
	public void onPreUpdate(final Job job)
	{
		entityManager.createQuery("DELETE FROM JobCandidateMatch jcm WHERE jcm.job = :job")
		             .setParameter("job", job)
		             .executeUpdate();
	}
}
