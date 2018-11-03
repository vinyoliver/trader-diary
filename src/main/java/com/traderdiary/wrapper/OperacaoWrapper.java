package com.traderdiary.wrapper;

import com.traderdiary.model.Finalidade;
import com.traderdiary.model.Operacao;
import com.traderdiary.utils.NumberUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OperacaoWrapper {

    private long id;
    private long codigo;
    private Finalidade finalidade;
    private String papel;
    private long quantidade;
    private BigDecimal entrada;
    private BigDecimal stop;
    private BigDecimal target;
    private BigDecimal saida;
    private LocalDateTime inicio;
    private LocalDateTime termino;

    public OperacaoWrapper(Operacao operacao) {
        this.codigo = operacao.getCodigo();
        this.id = operacao.getId();
        this.finalidade = operacao.getFinalidade();
        this.papel = operacao.getPapel();
        this.quantidade = operacao.getQuantidade();
        this.entrada = operacao.getPrecoEntrada();
        this.stop = operacao.getStopLoss().get(0).getValor();
        this.target = operacao.getStopGain().get(0).getValor();
        this.saida = operacao.getPrecoSaida();
        this.inicio = operacao.getDataInicio();
        this.termino = operacao.getDataTermino();
    }


    public BigDecimal getPercentual() {
        if (termino == null) {
            return null;
        } else if (finalidade == Finalidade.COMPRA) {
            return (saida.subtract(entrada)).divide(entrada, 4, BigDecimal.ROUND_HALF_UP).multiply(NumberUtils.CEM);
        } else {
            return (entrada.subtract(saida)).divide(saida, 4, BigDecimal.ROUND_HALF_UP).multiply(NumberUtils.CEM);
        }
    }

}
