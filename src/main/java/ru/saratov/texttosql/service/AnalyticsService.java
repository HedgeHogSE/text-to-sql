package ru.saratov.texttosql.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import ru.saratov.texttosql.tool.SqlExecutorTool;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AnalyticsService {

    private final ChatClient chatClient;
    private final SqlExecutorTool sqlExecutorTool;

    private static final Pattern SQL_PATTERN = Pattern.compile("```sql\\n(.*?)\\n```", Pattern.DOTALL);

    public AnalyticsService(ChatClient chatClient, SqlExecutorTool sqlExecutorTool) {
        this.chatClient = chatClient;
        this.sqlExecutorTool = sqlExecutorTool;
    }

    public String ask(String question) {
        String rawResponse = chatClient.prompt()
                .user(question)
                .call()
                .content();

        Matcher matcher = SQL_PATTERN.matcher(rawResponse);
        if (matcher.find()) {
            String sql = matcher.group(1).trim();
            try {
                List<Map<String, Object>> result = sqlExecutorTool.executeSql(sql);
                return formatResult(sql, question, result);
            } catch (Exception e) {
                return "Ошибка при выполнении запроса: " + e.getMessage();
            }
        }

        return rawResponse;
    }

    private String formatResult(String sql, String question, List<Map<String, Object>> result) {
        if (result == null || result.isEmpty()) {
            return "Пустой результат";
        }

        String sqlUpper = sql.toUpperCase();
        String aggType = detectAggregationType(sqlUpper);
        String city = extractCity(sql);

        if (result.size() == 1) {
            Map<String, Object> row = result.get(0);
if (row.size() == 1) {
                Object value = row.values().iterator().next();
                if (value == null) return "Пустой результат";

                return formatSingleValue(aggType, city, value.toString());
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> row : result) {
            sb.append(row).append("\n");
        }
        return sb.toString().trim();
    }

    private String detectAggregationType(String sql) {
        if (sql.contains("COUNT(")) return "COUNT";
        if (sql.contains("SUM(")) return "SUM";
        if (sql.contains("AVG(")) return "AVG";
        if (sql.contains("MAX(")) return "MAX";
        if (sql.contains("MIN(")) return "MIN";
        return "LIST";
    }

    private String extractCity(String sql) {
        Pattern pattern = Pattern.compile("city\\s*=\\s*['\"](.*?)['\"]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String formatSingleValue(String aggType, String city, String value) {
        switch (aggType) {
            case "COUNT":
                if (city != null) {
                    return "В базе " + value + " пользовател" + getGenderEnding(Integer.parseInt(value)) + " из " + city + ".";
                }
                return "Всего " + value + " запис" + getRecordEnding(Integer.parseInt(value)) + " в базе.";

            case "AVG":
                double avgValue = Double.parseDouble(value);
                String formatted = String.format("%.0f", avgValue);
                if (city != null) {
                    return "Средний чек в " + city + " — " + formatted + " ₽.";
                }
                return "Среднее значение — " + formatted + ".";

            case "SUM":
                double sumValue = Double.parseDouble(value);
                String formattedSum = String.format("%,.0f", sumValue);
                return "Общая сумма — " + formattedSum + " ₽.";

            case "MAX":
                return "Максимальное значение — " + value + ".";

            case "MIN":
                return "Минимальное значение — " + value + ".";

            default:
                return value;
        }
    }

    private String getGenderEnding(int count) {
        if (count % 100 >= 11 && count % 100 <= 19) return "ей";
        if (count % 10 == 1) return "ь";
        if (count % 10 >= 2 && count % 10 <= 4) return "я";
        return "ей";
    }

    private String getRecordEnding(int count) {
        if (count % 100 >= 11 && count % 100 <= 19) return "ей";
        if (count % 10 == 1) return "ь";
        if (count % 10 >= 2 && count % 10 <= 4) return "и";
        return "ей";
    }
}