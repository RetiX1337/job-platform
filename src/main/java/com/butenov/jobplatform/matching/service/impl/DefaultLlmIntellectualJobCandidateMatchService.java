package com.butenov.jobplatform.matching.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.dto.CandidateMatchingDto;
import com.butenov.jobplatform.matching.dto.JobCandidateMatchDto;
import com.butenov.jobplatform.matching.dto.JobMatchingDto;
import com.butenov.jobplatform.matching.service.LlmIntellectualJobCandidateMatchService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Service
@AllArgsConstructor
@Log
public class DefaultLlmIntellectualJobCandidateMatchService implements LlmIntellectualJobCandidateMatchService
{
	private final OpenAiChatModel openAiChatModel;
	private final ObjectMapper objectMapper;

	@Override
	public JobCandidateMatchDto calculateLlmIntellectualMatch(final Job job, final Candidate candidate)
	{
		try
		{
			final String promptTemplate = new String(Files.readAllBytes(
					new ClassPathResource("llm-prompts/job-candidate-match-prompt.txt").getFile().toPath()
			));

			final CandidateMatchingDto candidateMatchingDto = new CandidateMatchingDto(candidate.getCandidateProfile().getJobExperiences(),
					new ArrayList<>(candidate.getCandidateProfile().getSkills()), candidate.getCandidateProfile().getEducations());
			final JobMatchingDto jobMatchingDto = JobMatchingDto.builder()
			                                                    .title(job.getTitle())
			                                                    .description(job.getDescription())
			                                                    .requirements(job.getRequirements())
			                                                    .requiredSkills(new ArrayList<>(job.getRequiredSkills()))
			                                                    .build();
			final String candidateJson = objectMapper.writeValueAsString(candidateMatchingDto);
			final String jobJson = objectMapper.writeValueAsString(jobMatchingDto);

			final String fullPrompt = promptTemplate.replace("{{job}}", jobJson).replace("{{candidate}}", candidateJson);

			final String aiResponse = openAiChatModel.call(fullPrompt);

			final String cleanJson = aiResponse.replace("```", "").replace("json", "").trim();

			return objectMapper.readValue(cleanJson, JobCandidateMatchDto.class);
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
