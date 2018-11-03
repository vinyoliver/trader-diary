package com.traderdiary.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    public static DateTimeFormatter getFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return toZonedDateTime(date).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return toZonedDateTime(date).toLocalDateTime();
    }

    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        return toZonedDateTime(date).toLocalTime();
    }

    private static ZonedDateTime toZonedDateTime(Date date) {
        if (date == null) {
            return null;
        }
        if (date instanceof java.sql.Date) {
            return ZonedDateTime.of(((java.sql.Date) date).toLocalDate(),
                    LocalTime.MIN, ZoneId.systemDefault());
        }
        return date.toInstant().atZone(ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(date, getFormatter("yyyy-MM-dd hh:mm:ss"));
    }

    public static String localDateTimeToStringDate(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String localDateTimeToStringDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String localDateTimeToStringDateTime(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static String format(String format, LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    public static String localDateTimeToStringDateTimeFormatted(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        String data = DateUtils.localDateTimeToStringDate(date);
        String hora = date.format(DateTimeFormatter.ofPattern("HH:mm"));
        return data + " às " + hora;
    }

    public static boolean isBetween(LocalTime time, LocalTime inicio, LocalTime termino) {
        if (time != null && inicio != null && termino != null) {
            return time.compareTo(inicio) >= 0 && time.compareTo(termino) <= 0;
        }
        return false;
    }

    public static LocalDateTime endOfDay(LocalDateTime localDateTime) {
        return localDateTime.withHour(23).withMinute(59).withSecond(59);
    }

    public static LocalDateTime startOfDay(LocalDateTime localDateTime) {
        return localDateTime.withHour(00).withMinute(00).withSecond(00);
    }

    public static Long numberOfDays(LocalDate dataInicio, LocalDate dataFim) {
        return ChronoUnit.DAYS.between(dataInicio, dataFim);
    }

}
