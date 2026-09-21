package ru.saratov.texttosql.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SqlExecutorToolTest {

    @Autowired
    private ru.saratov.texttosql.tool.SqlExecutorTool sqlExecutorTool;

    @Test
    void testExecuteSqlWithSelect() {
    }

    @Test
    void testExecuteSqlRejectsNonSelect() {
        assertThrows(IllegalArgumentException.class, () -> sqlExecutorTool.executeSql("DROP TABLE users"));
    }
}