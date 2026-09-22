package ru.saratov.texttosql.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.saratov.texttosql.exception.SqlTimeoutException;

import java.util.List;
import java.util.Map;

@Component
public class TimeoutJdbcTemplate {

    private final JdbcTemplate jdbcTemplate;
    private static final int QUERY_TIMEOUT_SECONDS = 10;

    public TimeoutJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
    }

    public List<Map<String, Object>> queryForList(String sql) {
        RowMapper<Map<String, Object>> rowMapper = (rs, rowNum) -> {
            Map<String, Object> row = new java.util.HashMap<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            return row;
        };
        try {
            return jdbcTemplate.query(sql, rowMapper);
        } catch (org.springframework.dao.DataAccessException e) {
            throw new SqlTimeoutException("Таймаут выполнения запроса: превышено время ожидания " + QUERY_TIMEOUT_SECONDS + " сек");
        }
    }
}