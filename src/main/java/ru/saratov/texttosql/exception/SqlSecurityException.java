package ru.saratov.texttosql.exception;

public class SqlSecurityException extends RuntimeException {

    public SqlSecurityException(String message) {
        super(message);
    }
}