# ☁️ CloudStorage

**Облачное хранилище файлов с авторизацией, загрузкой и хранением через MinIO (S3).**

Проект разработан в рамках дипломной работы. Позволяет пользователям регистрироваться, входить в систему и управлять своими файлами через веб-интерфейс.

---

## 📌 Возможности

- Регистрация и вход в систему
- Загрузка и удаление файлов
- Хранение файлов в MinIO (аналог Amazon S3)
- Сохранение информации о пользователях и файлах в базе данных
- Аутентификация и защита маршрутов с помощью Spring Security

---

## 🧰 Стек технологий

| Компонент            | Технология                          |
|----------------------|--------------------------------------|
| Backend              | Spring Boot 2.2.4                    |
| View (шаблоны)       | Thymeleaf + Bootstrap                |
| Авторизация          | Spring Security                      |
| Работа с файлами     | MinIO SDK (Java)                     |
| База данных          | H2 (в памяти) + MyBatis              |
| HTTP-клиент          | OkHttp                               |
| Контейнеризация      | Docker + Docker Compose              |
| Язык программирования| Java 11                              |

---

## 📂 Структура проекта

```bash
cloudstorage/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/djontlong/cloudstorage/
│   │   │   ├── config/         # Конфигурации (MinIO, Security)
│   │   │   ├── controller/     # Контроллеры Spring MVC
│   │   │   ├── model/          # Сущности и DTO
│   │   │   ├── mapper/         # MyBatis интерфейсы
│   │   │   └── service/        # Сервисы (логика загрузки и т.д.)
│   │   └── resources/
│   │       ├── templates/      # HTML-шаблоны (Thymeleaf)
│   │       └── application.properties
└── README.md
```
---

## 🚀 Запуск проекта

```bash
# Клонируй репозиторий
git clone https://github.com/yourusername/cloudstorage.git
cd cloudstorage

# Запусти контейнеры
docker-compose up --build

Доступ через:
http://localhost:8080

# 🐳 MinIO
Локальный сервер MinIO поднимается в docker-compose.
Админ-интерфейс доступен по адресу:
http://localhost:9000
