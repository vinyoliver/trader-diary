package com.traderdiary.rest.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.traderdiary.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(DateUtils.getFormatter("yyyy-MM-dd HH:mm:ss").format(value));
    }
}
