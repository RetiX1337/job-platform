package com.butenov.jobplatform.candidates.controller;

import java.util.ArrayList;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.butenov.jobplatform.candidates.dto.CandidateProfileEditingDto;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.service.CandidateService;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.service.SkillService;
import com.butenov.jobplatform.users.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/candidates")
public class CandidateController
{
	private final CandidateService candidateService;
	private final UserService userService;
	private final SkillService skillService;

	@GetMapping("/{id}")
	public String viewProfile(final Model model, final @PathVariable Long id)
	{
		final Candidate candidate = candidateService.findById(id);
		model.addAttribute("candidate", candidate);
		return "candidates/profile";
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@GetMapping("/edit")
	public String editProfile(final Model model, @AuthenticationPrincipal final UserDetails userDetails)
	{
		final Candidate candidate = (Candidate) userService.findByEmail(userDetails.getUsername());

		if (candidate.getEducations() == null)
		{
			candidate.setEducations(new ArrayList<>());
		}
		if (candidate.getJobExperiences() == null)
		{
			candidate.setJobExperiences(new ArrayList<>());
		}

		final CandidateProfileEditingDto candidateProfileEditingDto =
				CandidateProfileEditingDto.builder()
				                          .cvLink(candidate.getCvLink())
				                          .firstName(candidate.getFirstName())
				                          .lastName(candidate.getLastName())
				                          .jobExperienceList(new ArrayList<>(candidate.getJobExperiences()))
				                          .educationList(new ArrayList<>(candidate.getEducations()))
				                          .skillIds(candidate.getSkills().stream().map(Skill::getId).toList())
				                          .build();

		model.addAttribute("candidateProfileEditingDto", candidateProfileEditingDto);
		model.addAttribute("skills", skillService.findAll());

		return "candidates/edit-profile";
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/edit")
	public String updateProfile(
			@ModelAttribute("candidateProfileEditingDto") final CandidateProfileEditingDto candidateProfileEditingDto,
			@AuthenticationPrincipal final UserDetails userDetails)
	{
		final Candidate candidate = (Candidate) userService.findByEmail(userDetails.getUsername());

		candidate.setFirstName(candidateProfileEditingDto.getFirstName());
		candidate.setLastName(candidateProfileEditingDto.getLastName());
		candidate.setCvLink(candidateProfileEditingDto.getCvLink());

		if (candidateProfileEditingDto.getJobExperienceList() != null)
		{
			candidate.getJobExperiences().clear();
			candidateProfileEditingDto.getJobExperienceList()
			                          .forEach(jobExperience -> jobExperience.setCandidate(candidate));
			candidate.getJobExperiences().addAll(candidateProfileEditingDto.getJobExperienceList());
		}

		if (candidateProfileEditingDto.getEducationList() != null)
		{
			candidate.getEducations().clear();
			candidateProfileEditingDto.getEducationList()
			                          .forEach(education -> education.setCandidate(candidate));
			candidate.getEducations().addAll(candidateProfileEditingDto.getEducationList());
		}

		if (candidateProfileEditingDto.getSkillIds() != null)
		{
			candidate.getSkills().clear();
			candidateProfileEditingDto.getSkillIds()
			                          .forEach(skillId -> candidate.getSkills().add(skillService.findById(skillId)));
		}

		candidateService.save(candidate);

		return "redirect:/candidates/" + candidate.getId();
	}
}
