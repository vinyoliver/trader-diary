package com.traderdiary.exception;

import java.util.ArrayList;
import java.util.List;

public class AppRuntimeException extends RuntimeException {

    protected List<AppExceptionItem> messages;

    public AppRuntimeException() {
        messages = new ArrayList<>();
    }

    public AppRuntimeException(List<String> messages) {
        this();
        for (String message : messages) {
            this.messages.add(new AppExceptionItem(message));
        }
    }

    public AppRuntimeException(String message, String... args) {
        this();
        messages.add(new AppExceptionItem(message, args));
    }

    public void addMessage(String message, String... args) {
        messages.add(new AppExceptionItem(message, args));
    }

}
