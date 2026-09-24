package ru.otus.java.pro.hw08.dataprocessor;

public class FileProcessException extends RuntimeException {

    public FileProcessException(Exception cause) {
        super(cause);
    }

    public FileProcessException(String message) {
        super(message);
    }
}
