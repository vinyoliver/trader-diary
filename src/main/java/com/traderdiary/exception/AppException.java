package com.traderdiary.exception;

import lombok.Data;

import javax.ejb.ApplicationException;
import java.util.ArrayList;
import java.util.List;

@Data
@ApplicationException(rollback = true)
public class AppException extends RuntimeException {

    protected List<AppExceptionItem> messages;

    public AppException() {
        messages = new ArrayList<>();
    }

    public AppException(List<String> messages) {
        this();
        for (String message : messages) {
            this.messages.add(new AppExceptionItem(message));
        }
    }

    public AppException(String message, String... args) {
        this();
        messages.add(new AppExceptionItem(message, args));
    }

    public void addMessage(String message, String... args) {
        messages.add(new AppExceptionItem(message, args));
    }
}
