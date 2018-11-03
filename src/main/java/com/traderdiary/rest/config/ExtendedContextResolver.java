package com.traderdiary.rest.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.traderdiary.rest.converter.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ExtendedContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ExtendedContextResolver() {
        this.mapper = new ObjectMapper();

        this.mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, Boolean.TRUE);
        this.mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, Boolean.TRUE);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
        this.mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, Boolean.TRUE);
        this.mapper.configure(MapperFeature.USE_ANNOTATIONS, Boolean.TRUE);
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, Boolean.FALSE);

        mapper.registerModule(configureModule());


    }

    @Override
    public ObjectMapper getContext(Class<?> clazz) {
        return mapper;
    }

    public static SimpleModule configureModule() {
        SimpleModule moduleConverters = new SimpleModule();

        moduleConverters.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        moduleConverters.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());

        moduleConverters.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        moduleConverters.addSerializer(LocalDate.class, new CustomLocalDateSerializer());

        moduleConverters.addDeserializer(LocalTime.class, new CustomLocalTimeDeserializer());
        moduleConverters.addSerializer(LocalTime.class, new CustomLocalTimeSerializer());

        moduleConverters.addDeserializer(BigDecimal.class, new CustomBigDecimalDeserializer());
        moduleConverters.addDeserializer(String.class, new CustomStringDeserializer());

        return moduleConverters;
    }
}