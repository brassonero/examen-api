package com.prueba.exception;

public class ExamAlreadyAnsweredException extends RuntimeException {
    public ExamAlreadyAnsweredException(String message) {
        super(message);
    }
}
