package ru.saratov.texttosql.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.saratov.texttosql.exception.InvalidSqlException;
import ru.saratov.texttosql.exception.SqlSecurityException;
import ru.saratov.texttosql.exception.SqlTimeoutException;

import java.util.List;
import java.util.Map;

@Component
public class SqlExecutorTool {

    private JdbcTemplate jdbcTemplate;

    private static final int QUERY_TIMEOUT_SECONDS = 10;
    private static final int MAX_RESULT_LIMIT = 100;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeSql(String sql) {
        String normalized = sql.trim().toUpperCase();

        if (normalized.contains("DROP") || normalized.contains("DELETE") 
                || normalized.contains("UPDATE") || normalized.contains("INSERT")
                || normalized.contains("TRUNCATE") || normalized.contains("ALTER")) {
            throw new SqlSecurityException("Запрещенная операция: модифицирующие запросы заблокированы");
        }

        if (!normalized.startsWith("SELECT")) {
            throw new InvalidSqlException("Разрешены только SELECT-запросы");
        }

        if (normalized.contains("LIMIT")) {
            return jdbcTemplate.queryForList(sql);
        }

        String limitedSql = sql.replaceFirst(";\\s*$", "") + " LIMIT " + MAX_RESULT_LIMIT;
        return jdbcTemplate.queryForList(limitedSql);
    }
}