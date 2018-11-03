package com.traderdiary.wrapper;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class ResumoWrapper {

    private Integer mes;
    private BigDecimal gain30Dias;
    private BigDecimal loss30Dias;

    private BigDecimal gain6Meses;
    private BigDecimal loss6Meses;

    public ResumoWrapper(BigDecimal gain30Dias, BigDecimal loss30Dias, BigDecimal gain6Meses, BigDecimal loss6Meses) {
        this.gain30Dias = gain30Dias;
        this.loss30Dias = loss30Dias;
        this.gain6Meses = gain6Meses;
        this.loss6Meses = loss6Meses;
    }

    public ResumoWrapper(Integer mes, BigDecimal gain30Dias, BigDecimal loss30Dias) {
        this.mes = mes;
        this.gain30Dias = gain30Dias;
        this.loss30Dias = loss30Dias;
    }
}
