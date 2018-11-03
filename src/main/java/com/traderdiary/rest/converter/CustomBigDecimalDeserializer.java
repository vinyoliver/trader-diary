package com.traderdiary.rest.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

public class CustomBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    public BigDecimal deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String valor = jsonparser.getText();
        if (StringUtils.isEmpty(valor)) {
            return null;
        }
        if (valor.indexOf(",") == -1) {
            return new BigDecimal(valor);
        }
        valor = valor.replaceAll("[.]", "").replaceAll(",", ".");
        return new BigDecimal(valor);
    }
}