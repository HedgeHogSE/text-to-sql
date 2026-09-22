# Text-to-SQL Analytics Service

AI-powered REST-сервис на Spring Boot, который принимает вопросы на естественном языке, генерирует SQL-запрос к базе данных и возвращает структурированный ответ.

## 🚀 Старт

### Требования

- Java 17+
- Maven 3.8+
- PostgreSQL 14+ (или запуск через Docker)
- Ollama (для локального LLM)

### Запуск через Docker (рекомендуется)

```bash
# Запуск PostgreSQL
docker run -d --name text-sql-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=text_sql_db \
  -p 5432:5432 \
  postgres:16-alpine

# Запуск Ollama с llama3
docker run -d --name ollama \
  -p 11434:11434 \
  -v ollama:/root/.ollama \
  ollama/ollama

docker exec -it ollama ollama pull llama3
```

```bash
# Клонирование и запуск приложения
git clone <репозиторий>
cd text-to-sql

# Обновление переменных окружения
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/text_sql_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export SPRING_AI_OPENAI_API_KEY=dummy-key
export SPRING_AI_OPENAI_BASE_URL=http://localhost:11434/v1
export SPRING_AI_OPENAI_MODEL=llama3

# Запуск
mvn spring-boot:run
```

### Локальный запуск без Docker

1. Установите PostgreSQL и создайте базу `text_sql_db`
2. Установите Ollama и загрузите модель `llama3`
3. Запускайте с теми же переменными окружения

## 📡 API-endpoints

### `POST /api/analytics/ask`

Передаёт вопрос на естественном языке и получает ответ.

**Request:**
```json
{
  "question": "Сколько всего пользователей?"
}
```

**Response:**
```json
{
  "question": "Сколько всего пользователей?",
  "answer": "В базе 50 пользователей."
}
```

### `POST /api/chat/generate`

Простой эндпоинт без QL (для демонстрации работы LLM).

**Request:**
```json
{
  "message": "Привет!"
}
```

**Response:**
```json
{
  "message": "Привет!",
  "answer": "Привет! Чем могу помочь?"
}
```

## 📝 Примеры запросов

| Вопрос | Ожидаемый ответ |
|--------|----------------|
| `Сколько всего пользователей?` | `В базе 50 пользователей.` |
| `Сколько пользователей из Москвы?` | `В базе 15 пользователей из Москвы.` |
| `Какая средняя цена товаров в категории Electronics?` | `Средняя цена — 2345 ₽.` |
| `Топ-3 самых дорогих товаров?` | `MacBook Pro 14" (1999.99 ₽), Dell XPS 15 (1799.99 ₽), Sony WH-1000XM5 (349.99 ₽).` |
| `Какое количество заказов было отменено?` | `Отменено 5 заказов.` |
| `Какой сегодня день?` | `Я отвечаю только на вопросы о базе данных.` |

## 🗄️ Схема БД

```
users (id, username, email, city, created_at)
products (id, name, category, price)
orders (id, user_id, status, created_at)
order_items (id, order_id, product_id, quantity, total_price)
```

## 🔒 Безопасность

- Разрешены только `SELECT` запросы
- Заблокированы: `DROP`, `DELETE`, `UPDATE`, `INSERT`, `TRUNCATE`, `ALTER`
- Максимум 100 строк в результате (LIMIT)
- Таймаут запроса: 10 секунд

## 🧪 Тесты

```bash
# Unit-тесты
mvn test -Dtest=SqlExecutorToolTest

# Компиляция
mvn clean compile
```

## 🏗️ Архитектура

```
QuestionController → AnalyticsService → ChatClient (Spring AI) → SqlExecutorTool → PostgreSQL
```

## ⚙️ Конфигурация

Основные переменные окружения:

| Переменная | Значение по умолчанию | Описание |
|------------|----------------------|----------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/text_sql_db` | Подключение к БД |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Имя пользователя |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Пароль |
| `SPRING_AI_OPENAI_API_KEY` | `dummy-key` | Ключ OpenAI (не используется с Ollama) |
| `SPRING_AI_OPENAI_BASE_URL` | `http://localhost:11434/v1` | URL Ollama |
| `SPRING_AI_OPENAI_MODEL` | `llama3` | Модель LLM |

## 📚 Документация

- `docs/proposal-text-to-sql.md` — техническое задание
- `src/main/resources/system-prompt.txt` — системный промпт для LLM