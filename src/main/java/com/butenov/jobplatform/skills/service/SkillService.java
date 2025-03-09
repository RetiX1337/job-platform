package com.butenov.jobplatform.skills.service;

import java.util.List;

import com.butenov.jobplatform.skills.model.Skill;

public interface SkillService
{
	Skill findById(Long id);

	List<Skill> findAll();
}
