package com.butenov.jobplatform.skills.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.repository.SkillRepository;
import com.butenov.jobplatform.skills.service.SkillService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DefaultSkillService implements SkillService
{

	private final SkillRepository skillRepository;

	public DefaultSkillService(final SkillRepository skillRepository) {
		this.skillRepository = skillRepository;
	}

	@Override
	public Skill findById(final Long id) {
		return skillRepository.findById(id)
		                      .orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + id));
	}

	@Override
	public List<Skill> findAll()
	{
		return skillRepository.findAll();
	}
}

