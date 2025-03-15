package com.butenov.jobplatform.skills.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.butenov.jobplatform.skills.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long>
{
	@Query("SELECT s FROM Skill s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :skillName, '%'))")
	Optional<Skill> findBySimilarName(@Param("skillName") String skillName);
}
