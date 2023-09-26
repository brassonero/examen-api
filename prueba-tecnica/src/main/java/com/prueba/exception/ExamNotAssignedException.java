package com.prueba.exception;

public class ExamNotAssignedException extends RuntimeException {
    public ExamNotAssignedException(String message) {
        super(message);
    }
}
