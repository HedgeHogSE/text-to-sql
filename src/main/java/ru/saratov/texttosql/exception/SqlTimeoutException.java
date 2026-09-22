package ru.saratov.texttosql.exception;

public class SqlTimeoutException extends RuntimeException {

    public SqlTimeoutException(String message) {
        super(message);
    }
}