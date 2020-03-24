package ru.geekbrains.supershop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UploadingInvalidFileFormatException extends Exception {

    private static final long serialVersionUID = 1L;

    public UploadingInvalidFileFormatException(String message) {
        super(message);
    }

}