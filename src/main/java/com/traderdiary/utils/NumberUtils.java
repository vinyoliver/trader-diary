package com.traderdiary.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    public static final BigDecimal CEM = new BigDecimal("100.0");

    public static String bigDecimalToString(BigDecimal number) {
        if (number == null) {
            return null;
        }
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(number);
    }

    public static boolean isPositivo(BigDecimal valorSaida) {
        return valorSaida != null && valorSaida.compareTo(BigDecimal.ZERO) > 0;
    }
}
