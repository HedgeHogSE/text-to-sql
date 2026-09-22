package ru.saratov.texttosql.tool;

import org.junit.jupiter.api.Test;
import ru.saratov.texttosql.exception.InvalidSqlException;
import ru.saratov.texttosql.exception.SqlSecurityException;

import static org.junit.jupiter.api.Assertions.*;

class SqlExecutorToolTest {

    private final SqlExecutorTool sqlExecutorTool = new SqlExecutorTool(null);

    @Test
    void testExecuteSqlRejectsDrop() {
        SqlSecurityException exception = assertThrows(SqlSecurityException.class, () -> 
                sqlExecutorTool.executeSql("DROP TABLE users"));
        assertTrue(exception.getMessage().contains("Запрещенная операция"));
    }

    @Test
    void testExecuteSqlRejectsDelete() {
        SqlSecurityException exception = assertThrows(SqlSecurityException.class, () -> 
                sqlExecutorTool.executeSql("DELETE FROM users"));
        assertTrue(exception.getMessage().contains("Запрещенная операция"));
    }

    @Test
    void testExecuteSqlRejectsUpdate() {
        SqlSecurityException exception = assertThrows(SqlSecurityException.class, () -> 
                sqlExecutorTool.executeSql("UPDATE users SET username = 'test'"));
        assertTrue(exception.getMessage().contains("Запрещенная операция"));
    }

    @Test
    void testExecuteSqlRejectsInsert() {
        SqlSecurityException exception = assertThrows(SqlSecurityException.class, () -> 
                sqlExecutorTool.executeSql("INSERT INTO users (username) VALUES ('test')"));
        assertTrue(exception.getMessage().contains("Запрещенная операция"));
    }

    @Test
    void testExecuteSqlRejectsTruncate() {
        SqlSecurityException exception = assertThrows(SqlSecurityException.class, () -> 
                sqlExecutorTool.executeSql("TRUNCATE TABLE users"));
        assertTrue(exception.getMessage().contains("Запрещенная операция"));
    }

    @Test
    void testExecuteSqlRejectsAlter() {
        SqlSecurityException exception = assertThrows(SqlSecurityException.class, () -> 
                sqlExecutorTool.executeSql("ALTER TABLE users ADD COLUMN test VARCHAR(100)"));
        assertTrue(exception.getMessage().contains("Запрещенная операция"));
    }

    @Test
    void testExecuteSqlRejectsNonSelect() {
        SqlSecurityException exception = assertThrows(SqlSecurityException.class, () -> 
                sqlExecutorTool.executeSql("INSERT INTO users (username) VALUES ('test')"));
        assertTrue(exception.getMessage().contains("Запрещенная операция"));
    }
}