package com.butenov.jobplatform.candidates.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PreUpdate;

public class CandidateEntityListener
{
	@PersistenceContext
	private EntityManager entityManager;

	@PreUpdate
	public void onPreUpdate(final Candidate candidate)
	{
		entityManager.createQuery("DELETE FROM JobCandidateMatch jcm WHERE jcm.candidate = :candidate")
		             .setParameter("candidate", candidate)
		             .executeUpdate();
	}
}
