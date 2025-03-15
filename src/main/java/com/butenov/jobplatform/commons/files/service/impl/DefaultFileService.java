package com.butenov.jobplatform.commons.files.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.butenov.jobplatform.commons.files.service.FileService;

@Service
public class DefaultFileService implements FileService
{
	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	public String storeFile(final MultipartFile file) throws IOException
	{
		final Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath))
		{
			Files.createDirectories(uploadPath);
		}

		final String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		final Path filePath = uploadPath.resolve(fileName);

		file.transferTo(filePath.toFile());
		return filePath.toString();
	}
}
