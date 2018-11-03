package com.traderdiary.exception;

import lombok.Data;

@Data
public class AppExceptionItem {

    public String key;
    public String args[];

    public AppExceptionItem(String key, String... args) {
        super();
        this.key = key;
        this.args = args;
    }

    public AppExceptionItem(String key) {
        super();
        this.key = key;
    }
}
