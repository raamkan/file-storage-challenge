package com.file.storage.service.filestorageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoFileRecordFoundException extends RuntimeException {

    public NoFileRecordFoundException(String message) {
        super(message);
    }

}
