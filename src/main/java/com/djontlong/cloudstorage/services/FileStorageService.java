package com.djontlong.cloudstorage.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileStorageService {

    private final MinioClient minioClient;

    // Конструктор для инициализации MinioClient с переменными окружения
    public FileStorageService(
            @Value("${minio.endpoint}") String minioEndpoint,
            @Value("${minio.access-key}") String minioAccessKey,
            @Value("${minio.secret-key}") String minioSecretKey) {
        // Создаём клиент для подключения к MinIO
        this.minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }

    // Метод для сохранения файла в MinIO
    public String storeFile(MultipartFile file) throws Exception {
        try {
            // Генерируем уникальное имя файла с помощью UUID
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            // Получаем InputStream из MultipartFile
            InputStream inputStream = file.getInputStream();

            // Загружаем файл в бакет "cloudstorage-files"
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("cloudstorage-files")
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            // Возвращаем имя файла
            return fileName;
        } catch (MinioException e) {
            // Обработка ошибок MinIO
            throw new RuntimeException("Ошибка при загрузке файла в MinIO: " + e.getMessage());
        }
    }
}