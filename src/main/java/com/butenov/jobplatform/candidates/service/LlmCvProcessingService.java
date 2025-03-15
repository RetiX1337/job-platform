package com.butenov.jobplatform.candidates.service;

import com.butenov.jobplatform.candidates.dto.LlmCvProcessingDto;

public interface LlmCvProcessingService
{
	LlmCvProcessingDto processCv(String cvLink);
}
