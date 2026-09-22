package ru.saratov.texttosql.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.saratov.texttosql.dto.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSqlException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSql(InvalidSqlException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                "Invalid SQL",
                ex.getMessage(),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(SqlTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleSqlTimeout(SqlTimeoutException ex) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new ErrorResponse(
                "Query timeout",
                ex.getMessage(),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(SqlSecurityException.class)
    public ResponseEntity<ErrorResponse> handleSqlSecurity(SqlSecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(
                "Security violation",
                ex.getMessage(),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponse(
                "Internal server error",
                ex.getMessage(),
                LocalDateTime.now()
        ));
    }
}