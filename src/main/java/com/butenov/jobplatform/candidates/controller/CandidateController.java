package com.butenov.jobplatform.candidates.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
import com.butenov.jobplatform.candidates.service.LlmCvProcessingService;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.service.SkillService;
import com.butenov.jobplatform.users.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
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
	@GetMapping("/me")
	public String viewProfileForCurrentCandidate(final Model model, @AuthenticationPrincipal final UserDetails userDetails)
	{
		final Candidate candidate = (Candidate) userService.findByEmail(userDetails.getUsername());
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
			@ModelAttribute("candidateProfileEditingDto") final CandidateProfileEditingDto candidateProfileEditingDto,
			@AuthenticationPrincipal final UserDetails userDetails)
	{
		final Candidate candidate = (Candidate) userService.findByEmail(userDetails.getUsername());

		candidate.setFirstName(candidateProfileEditingDto.getFirstName());
		candidate.setLastName(candidateProfileEditingDto.getLastName());

		candidate.getJobExperiences().clear();
		if (candidateProfileEditingDto.getJobExperienceList() != null)
		{
			candidateProfileEditingDto.getJobExperienceList()
			                          .forEach(jobExperience -> jobExperience.setCandidate(candidate));
			candidate.getJobExperiences().addAll(candidateProfileEditingDto.getJobExperienceList());
		}

		candidate.getEducations().clear();
		if (candidateProfileEditingDto.getEducationList() != null)
		{
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

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/upload-cv")
	public ResponseEntity<String> uploadCV(
			@RequestParam("cvFile") final MultipartFile file,
			@AuthenticationPrincipal final UserDetails userDetails)
	{
		try
		{
			final Candidate candidate = (Candidate) userService.findByEmail(userDetails.getUsername());

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

	@PostMapping("/update-from-cv")
	public String updateCandidateFromCV(@AuthenticationPrincipal final UserDetails userDetails, final Model model)
	{
		try
		{
			final Candidate candidate = (Candidate) userService.findByEmail(userDetails.getUsername());
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
				                          .id(candidate.getId())
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
}
