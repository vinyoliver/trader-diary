package com.traderdiary.rest.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.traderdiary.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    public LocalTime deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        try {
            if (StringUtils.isEmpty(date)) {
                return null;
            }
            date = date.replace(":", "");
            return LocalTime.parse(date, DateUtils.getFormatter("HHmm"));
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }

}