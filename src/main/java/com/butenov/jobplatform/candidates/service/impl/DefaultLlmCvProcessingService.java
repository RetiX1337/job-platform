package com.butenov.jobplatform.candidates.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.dto.LlmCvProcessingDto;
import com.butenov.jobplatform.candidates.service.LlmCvProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultLlmCvProcessingService implements LlmCvProcessingService
{
	private final OpenAiChatModel openAiChatModel;
	private final ObjectMapper objectMapper;

	@Override
	public LlmCvProcessingDto processCv(final String cvLink)
	{
		try
		{
			final String promptTemplate = new String(Files.readAllBytes(
					new ClassPathResource("llm-prompts/cv-parse-prompt.txt").getFile().toPath()
			));

			final String extractedText = extractTextFromPDF(cvLink);

			final String fullPrompt = promptTemplate + extractedText;

			final String aiResponse = openAiChatModel.call(fullPrompt);
			final String cleanJson = aiResponse.replace("```", "").trim();

			return objectMapper.readValue(cleanJson, LlmCvProcessingDto.class);
		}
		catch (final Exception e)
		{
			throw new RuntimeException("Error processing resume", e);
		}
	}

	private String extractTextFromPDF(final String filePath) throws IOException
	{
		final File file = new File(filePath);
		if (!file.exists())
		{
			throw new IOException("File not found: " + filePath);
		}

		try (final PDDocument document = PDDocument.load(file))
		{
			final PDFTextStripper pdfStripper = new PDFTextStripper();
			return pdfStripper.getText(document).replaceAll("\\s+", " "); // Convert to one line
		}
	}
}
