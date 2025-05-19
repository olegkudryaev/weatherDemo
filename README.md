# Weather Demo Application

## Запуск приложения

### Запуск в Docker

1. Убедитесь, что у вас установлен Docker и Docker Compose
2. Запустите приложение и базу данных:
```bash
docker-compose up -d
```

Приложение будет доступно по адресу: http://localhost:8070

### Запуск локально

1. Убедитесь, что у вас установлена Java 17
2. Запустите PostgreSQL через Docker:
```bash
docker-compose up -d postgres
```
3. Запустите приложение:
```bash
./gradlew bootRun
```

## Swagger UI

Документация API доступна по адресу:
- Swagger UI: http://localhost:8070/swagger-ui
- OpenAPI спецификация: http://localhost:8070/v3/api-docs

## База данных

### Параметры подключения
- Host: localhost
- Port: 5432
- Database: weatherdb
- Username: postgres
- Password: postgres

### Схема базы данных
База данных содержит следующие таблицы:
- `raw_weather_data` - сырые данные о погоде
- `normalized_weather_data` - нормализованные данные о погоде

## Важная информация

### Профили приложения
- `prod` - продакшн профиль (используется в Docker)
- `test` - тестовый профиль (используется для тестов)

### Тестирование
Для запуска тестов:
```bash
./gradlew test
```

### Мониторинг
- Hibernate SQL логирование включено в продакшн профиле
- Форматированный SQL вывод для удобства отладки

### Масштабирование
Приложение поддерживает параллельную обработку данных от нескольких источников погоды (настраивается через `weather-properties.weather-source.sourceCount`)
