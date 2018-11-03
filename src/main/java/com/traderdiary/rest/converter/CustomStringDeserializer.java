package com.traderdiary.rest.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class CustomStringDeserializer extends JsonDeserializer<String> {

    public String deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String value = jsonparser.getText();
        return StringUtils.isEmpty(value) ? null : value;
    }
}