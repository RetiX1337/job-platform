package com.butenov.jobplatform.candidates.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.butenov.jobplatform.candidates.dto.CandidateProfileEditingDto;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.service.CandidateService;
import com.butenov.jobplatform.candidates.service.CandidateUtil;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.service.SkillService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@AllArgsConstructor
@Controller
@RequestMapping("/candidates")
public class CandidateController
{
	private final CandidateService candidateService;
	private final SkillService skillService;
	private final CandidateUtil candidateUtil;

	@GetMapping("/{id}")
	public String viewProfile(final Model model, final @PathVariable Long id)
	{
		final Candidate candidate = candidateService.findById(id);
		model.addAttribute("candidate", candidate);
		return "candidates/profile";
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@GetMapping("/me")
	public String viewProfileForCurrentCandidate(final Model model)
	{
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();
		model.addAttribute("candidate", candidate);
		return "candidates/profile";
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@GetMapping("/edit")
	public String editProfile(final Model model, @AuthenticationPrincipal final UserDetails userDetails)
	{
		return prepareCandidateEditForm(model, userDetails);
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/edit")
	public String updateProfile(
			@ModelAttribute("candidateProfileEditingDto") final CandidateProfileEditingDto candidateProfileEditingDto)
	{
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();

		candidate.setFirstName(candidateProfileEditingDto.getFirstName());
		candidate.setLastName(candidateProfileEditingDto.getLastName());

		candidate.getCandidateProfile().getJobExperiences().clear();
		if (candidateProfileEditingDto.getJobExperienceList() != null)
		{
			candidateProfileEditingDto.getJobExperienceList()
			                          .forEach(jobExperience -> jobExperience.setCandidateProfile(candidate.getCandidateProfile()));
			candidate.getCandidateProfile().getJobExperiences().addAll(candidateProfileEditingDto.getJobExperienceList());
		}

		candidate.getCandidateProfile().getEducations().clear();
		if (candidateProfileEditingDto.getEducationList() != null)
		{
			candidateProfileEditingDto.getEducationList()
			                          .forEach(education -> education.setCandidateProfile(candidate.getCandidateProfile()));
			candidate.getCandidateProfile().getEducations().addAll(candidateProfileEditingDto.getEducationList());
		}

		if (candidateProfileEditingDto.getSkillIds() != null)
		{
			candidate.getCandidateProfile().getSkills().clear();
			candidateProfileEditingDto.getSkillIds()
			                          .forEach(skillId -> candidate.getCandidateProfile().getSkills().add(skillService.findById(skillId)));
		}

		candidateService.update(candidate);

		return "redirect:/candidates/" + candidate.getId();
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/upload-cv")
	public ResponseEntity<String> uploadCV(
			@RequestParam("cvFile") final MultipartFile file)
	{
		try
		{
			final Candidate candidate = candidateUtil.getAuthenticatedCandidate();

			candidateService.uploadCV(candidate, file);

			return ResponseEntity.ok().build();
		}
		catch (final RuntimeException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
		}
	}

	@GetMapping("/{id}/cv")
	public ResponseEntity<Resource> getCV(@PathVariable final Long id) throws IOException
	{
		final Candidate candidate = candidateService.findById(id);
		if (candidate.getCvLink() == null)
		{
			return ResponseEntity.notFound().build();
		}

		final Path filePath = Paths.get(candidate.getCvLink());
		if (!Files.exists(filePath))
		{
			return ResponseEntity.notFound().build();
		}

		final Resource resource = new UrlResource(filePath.toUri());
		final String filename = filePath.getFileName().toString();

		return ResponseEntity.ok()
		                     .contentType(MediaType.APPLICATION_OCTET_STREAM)
		                     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
		                     .body(resource);
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/update-from-cv")
	public String updateCandidateFromCV(@AuthenticationPrincipal final UserDetails userDetails, final Model model)
	{
		try
		{
			final Candidate candidate = candidateUtil.getAuthenticatedCandidate();
			if (candidate.getCvLink() == null || candidate.getCvLink().isEmpty())
			{
				model.addAttribute("error", "No CV found for this candidate.");
				return prepareCandidateEditForm(model, userDetails);
			}

			candidateService.updateCandidateFromCV(candidate);

			return "redirect:/candidates/" + candidate.getId();
		}
		catch (final Exception e)
		{
			log.warning("Error updating candidate from CV: " + e.getMessage());
			model.addAttribute("error", "An error occurred while updating your profile.");
			return prepareCandidateEditForm(model, userDetails);
		}
	}

	private String prepareCandidateEditForm(final Model model, final UserDetails userDetails)
	{
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();

		if (candidate.getCandidateProfile().getEducations() == null)
		{
			candidate.getCandidateProfile().setEducations(new ArrayList<>());
		}
		if (candidate.getCandidateProfile().getJobExperiences() == null)
		{
			candidate.getCandidateProfile().setJobExperiences(new ArrayList<>());
		}

		final CandidateProfileEditingDto candidateProfileEditingDto =
				CandidateProfileEditingDto.builder()
				                          .id(candidate.getId())
				                          .cvLink(candidate.getCvLink())
				                          .firstName(candidate.getFirstName())
				                          .lastName(candidate.getLastName())
				                          .jobExperienceList(new ArrayList<>(candidate.getCandidateProfile().getJobExperiences()))
				                          .educationList(new ArrayList<>(candidate.getCandidateProfile().getEducations()))
				                          .skillIds(candidate.getCandidateProfile().getSkills().stream().map(Skill::getId).toList())
				                          .build();

		model.addAttribute("candidateProfileEditingDto", candidateProfileEditingDto);
		model.addAttribute("skills", skillService.findAll());

		return "candidates/edit-profile";
	}
}
