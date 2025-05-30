# Базовый образ с Java 17
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл
COPY cloudstorage-0.0.1-SNAPSHOT.jar app.jar

# Создаём директорию для хранения файлов
RUN mkdir /app/uploads

# Открываем порт 8080
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]