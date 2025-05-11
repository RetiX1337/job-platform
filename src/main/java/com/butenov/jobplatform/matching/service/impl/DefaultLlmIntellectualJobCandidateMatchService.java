package com.butenov.jobplatform.matching.service.impl;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.matching.dto.JobCandidateMatchDto;
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
	public JobCandidateMatchDto calculateLlmIntellectualMatch(final String jobJson,
	                                                          final String candidateJson)
	{
		try
		{
			final String promptTemplate = new String(Files.readAllBytes(
					new ClassPathResource("llm-prompts/job-candidate-match-prompt.txt").getFile().toPath()
			));

			final String fullPrompt = promptTemplate.replace("{{job}}", jobJson).replace("{{candidate}}", candidateJson);

			final String aiResponse = openAiChatModel.call(fullPrompt);

			final String cleanJson = aiResponse.replace("```", "").replace("json", "").trim();
			log.info("AI response: " + cleanJson);

			return objectMapper.readValue(cleanJson, JobCandidateMatchDto.class);
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
