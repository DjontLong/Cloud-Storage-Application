package com.djontlong.cloudstorage.services;

import com.djontlong.cloudstorage.mapper.FileMapper;
import com.djontlong.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
	private final FileMapper fileMapper;
	private final MinioService minioService;

	public FileService(FileMapper fileMapper, MinioService minioService) {
		this.fileMapper = fileMapper;
		this.minioService = minioService;
	}

	public void addFile(MultipartFile multipartFile, Integer userId) throws Exception {
		String fileName = multipartFile.getOriginalFilename();
		String contentType = multipartFile.getContentType();
		long fileSize = multipartFile.getSize();

		// проверяем, существует ли файл с таким именем у пользователя
		if (fileMapper.findFileByName(fileName, userId) != null) {
			throw new IllegalArgumentException("File with this name already exists");
		}

		// загружаем файл в MinIO
		try (InputStream inputStream = multipartFile.getInputStream()) {
			minioService.uploadFile(fileName, inputStream, fileSize);
		}

		// сохраняем метаданные в базу данных
		File file = new File();
		file.setFileName(fileName);
		file.setContentType(contentType);
		file.setFileSize(String.valueOf(fileSize));
		file.setUserId(userId);
		fileMapper.insert(file);
	}

	public List<File> getAllFiles(Integer userId) {
		return fileMapper.getFilesByUser(userId);
	}

	public File getFile(Integer fileId) {
		File file = fileMapper.findFile(fileId);
		if (file == null) {
			throw new IllegalArgumentException("File not found");
		}
		return file;
	}

	public byte[] downloadFile(Integer fileId) throws Exception {
		File file = getFile(fileId);
		return minioService.downloadFile(file.getFileName());
	}

	public void deleteFile(Integer fileId) throws Exception {
		File file = getFile(fileId);

		// удаляем файл из MinIO
		minioService.deleteFile(file.getFileName());

		// удаляем запись из базы данных
		fileMapper.delete(fileId);
	}

	public boolean isFileNameAvailable(String fileName, Integer userId) {
		return fileMapper.findFileByName(fileName, userId) == null;
	}
}