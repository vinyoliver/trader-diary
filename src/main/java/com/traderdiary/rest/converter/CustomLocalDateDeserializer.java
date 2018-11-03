package com.traderdiary.rest.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.traderdiary.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        try {
            return LocalDate.parse(date, DateUtils.getFormatter("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }
}