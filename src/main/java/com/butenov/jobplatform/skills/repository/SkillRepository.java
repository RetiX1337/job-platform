package com.butenov.jobplatform.skills.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butenov.jobplatform.skills.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long>
{
}
