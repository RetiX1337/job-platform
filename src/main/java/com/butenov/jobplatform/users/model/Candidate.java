package com.butenov.jobplatform.users.model;

import java.util.HashSet;
import java.util.Set;

import com.butenov.jobplatform.skills.model.Skill;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate extends User
{

	@ManyToMany
	@JoinTable(
			name = "candidate_skills",
			joinColumns = @JoinColumn(name = "candidate_id"),
			inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> skills = new HashSet<>();
}

