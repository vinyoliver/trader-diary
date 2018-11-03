package com.traderdiary.rest.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.traderdiary.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;

public class CustomLocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(DateUtils.getFormatter("yyyy-MM-dd").format(value));
    }
}
