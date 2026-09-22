package ru.saratov.texttosql.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.saratov.texttosql.tool.SqlExecutorTool;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, SqlExecutorTool sqlExecutorTool) {
        return builder
                .defaultSystem("Ты — аналитик данных. Твоя задача — отвечать на вопросы пользователя о базе данных компании.\n" +
                        "\n" +
                        "Схема базы данных:\n" +
                        "- users(id, username, email, city, created_at)\n" +
                        "- products(id, name, category, price)\n" +
                        "- orders(id, user_id, status, created_at)\n" +
                        "- order_items(id, order_id, product_id, quantity, total_price)\n" +
                        "\n" +
                        "Правила:\n" +
                        "1. Используй ТОЛЬКО инструмент executeSql для получения данных.\n" +
                        "2. Генерируй только SELECT-запросы. Запрещены INSERT, UPDATE, DELETE, DROP.\n" +
                        "3. Если вопрос не относится к базе данных — вежливо откажись отвечать.\n" +
                        "4. В финальном ответе кратко опиши, что именно ты посчитал.\n" +
                        "5. Если данных нет — так и скажи, не выдумывай числа.\n" +
                        "6. Даты в БД в формате TIMESTAMP, используй стандартные SQL-функции.\n" +
                        "7. Отвечай на русском языке.")
                .defaultTools(sqlExecutorTool)
                .build();
    }
}