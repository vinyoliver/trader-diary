package com.traderdiary.converter;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.*;
import java.util.Date;

@Converter(autoApply = true)
public final class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalTime attribute) {
        if (attribute == null) {
            return null;
        }
        Instant instant = attribute.atDate(LocalDate.of(2015, 1, 1)).
                atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    @Override
    public LocalTime convertToEntityAttribute(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
    }
}