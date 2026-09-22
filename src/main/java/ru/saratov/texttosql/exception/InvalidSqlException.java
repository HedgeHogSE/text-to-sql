package ru.saratov.texttosql.exception;

public class InvalidSqlException extends RuntimeException {

    public InvalidSqlException(String message) {
        super(message);
    }
}