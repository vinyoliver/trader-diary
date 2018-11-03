package com.traderdiary.rest.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.traderdiary.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String date = jsonparser.getText();

        try {
            return LocalDateTime.parse(date, DateUtils.getFormatter("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            } catch (DateTimeParseException e1) {
                throw new RuntimeException(e);
            }
        }
    }
}