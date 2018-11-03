package com.traderdiary.service;

import com.traderdiary.model.Operacao;
import com.traderdiary.model.QOperacao;
import com.traderdiary.model.Usuario;
import com.traderdiary.security.AppContext;
import com.traderdiary.utils.NumberUtils;
import com.traderdiary.wrapper.ResumoWrapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class OperacaoService extends BaseService<Operacao> {

    @Inject
    private AppContext appContext;

    @Inject
    private ImagemOperacaoService imagemOperacaoService;


    @Transactional(TxType.NOT_SUPPORTED)
    public Operacao findById(Long id) {
        return findById(Operacao.class, id);
    }

    public void finalizar(Operacao operacao) {
        operacao.setEncerrada(true);
        save(operacao);
    }

    public Operacao save(Operacao operacao) {
        operacao.updateStopLossOrGain();
        tratarImagens(operacao);
        operacao.getStopGain().stream().forEach(s -> s.setOperacao(operacao));
        operacao.getStopLoss().stream().forEach(s -> s.setOperacao(operacao));
        operacao.setPapel(operacao.getPapel().trim());
        if (operacao.isNovo()) {
            operacao.setCodigo(gerarCodigo(operacao.getUsuario().getId()));
        }
        return super.save(operacao);
    }

    private void tratarImagens(Operacao operacao) {
        if (!operacao.isNovo()) {
            Operacao beforeUpdate = findById(operacao.getId());
            beforeUpdate.getImagens().forEach(im -> {
                if (!operacao.getImagens().contains(im)) {
                    imagemOperacaoService.remove(im);
                }
            });
            operacao.getImagens().forEach(i -> i.setOperacao(operacao));
        }
    }


    @Override
    public void remove(Operacao operacao) {
        operacao.getImagens().forEach(im -> imagemOperacaoService.remove(im));
        super.remove(operacao);
    }

    private Long gerarCodigo(Long usuarioId) {
        QOperacao qOperacao = QOperacao.operacao;
        return instanceJPQLQuery().from(qOperacao).where(qOperacao.usuario.id.eq(usuarioId)).count() + 1L;
    }

    public Long next(Long id) {
        Operacao operacao = findById(id);
        QOperacao qOperacao = QOperacao.operacao;
        try {
            return instanceJPQLQuery().from(qOperacao).where(qOperacao.usuario.id.eq(operacao.getUsuario().getId()).and(qOperacao.codigo.gt(operacao.getCodigo()))).limit(1).orderBy(qOperacao.codigo.asc()).list(qOperacao).get(0).getId();
        } catch (IndexOutOfBoundsException e) {
            return id;
        }

    }

    public long previous(Long id) {
        Operacao operacao = findById(id);
        QOperacao qOperacao = QOperacao.operacao;
        try {
            return instanceJPQLQuery().from(qOperacao).where(qOperacao.usuario.id.eq(operacao.getUsuario().getId()).and(qOperacao.codigo.lt(operacao.getCodigo()))).limit(1).orderBy(qOperacao.codigo.desc()).list(qOperacao).get(0).getId();
        } catch (IndexOutOfBoundsException e) {
            return id;
        }
    }

    public ResumoWrapper countGainLoss(String periodo) {
        LocalDateTime inicioPeriodo = LocalDateTime.now();
        if (periodo.equals("MES")) {
            inicioPeriodo = inicioPeriodo.minusDays(30);
        } else if (periodo.equals("SEMESTRE")) {
            inicioPeriodo = inicioPeriodo.minusMonths(6);
        } else {
            inicioPeriodo = inicioPeriodo.minusYears(1);
        }
        final List<Operacao> operacoes = findOperacoesNoPeriodo(appContext.getUsuarioLogado(), inicioPeriodo, LocalDateTime.now());
        BigDecimal gain = BigDecimal.ZERO;
        BigDecimal loss = BigDecimal.ZERO;
        for (Operacao o : operacoes) {
//            if(o.getFinalidade() == Finalidade.COMPRA){
//                entrada = entrada.add(o.getPrecoEntrada());
//                saida = saida.add(o.getPrecoSaida());
//            }else {
//                saida = entrada.add(o.getPrecoEntrada());
//                entrada = saida.add(o.getPrecoSaida());
//            }
            BigDecimal valorSaida = o.getValorSaida().multiply(new BigDecimal(o.getQuantidade()));
            if (NumberUtils.isPositivo(valorSaida)) {
                gain = gain.add(valorSaida);
            } else {
                loss = loss.add(valorSaida);
            }
        }

        loss = loss.multiply(new BigDecimal(-1));
        return new ResumoWrapper(gain, loss, gain, loss);
    }


    public List<ResumoWrapper> countGainLossYear(Integer ano) {
        LocalDateTime inicioPeriodo = LocalDateTime.of(ano != null ? ano : 2015, 1, 1, 0, 0, 0);
        LocalDateTime terminoPeriodo = LocalDateTime.of(ano != null ? ano : LocalDate.now().getYear(), 12, 31, 0, 0, 0);

        final List<Operacao> operacoes = findOperacoesNoPeriodo(appContext.getUsuarioLogado(), inicioPeriodo, terminoPeriodo);
        Map<Integer, List<Operacao>> operacoesPorPeriodo = operacoes.stream().collect(Collectors.groupingBy(op -> op.getDataTermino().getMonthValue()));
        List<ResumoWrapper> resumoOperacoes = new ArrayList<>();
        for (Map.Entry<Integer, List<Operacao>> entry : operacoesPorPeriodo.entrySet()) {
            BigDecimal gain = BigDecimal.ZERO;
            BigDecimal loss = BigDecimal.ZERO;
            for (Operacao o : entry.getValue()) {
                BigDecimal valorSaida = o.getValorSaida().multiply(new BigDecimal(o.getQuantidade()));
                if (NumberUtils.isPositivo(valorSaida)) {
                    gain = gain.add(valorSaida);
                } else {
                    loss = loss.add(valorSaida);
                }
            }
            loss = loss.multiply(new BigDecimal(-1));
            resumoOperacoes.add(new ResumoWrapper(entry.getKey(), gain, loss));
        }
        return resumoOperacoes;
    }

    private List<Operacao> findOperacoesNoPeriodo(Usuario usuario, LocalDateTime inicioPeriodo, LocalDateTime terminoPeriodo) {
        QOperacao qOperacao = QOperacao.operacao;
        return instanceJPQLQuery().from(qOperacao).where(qOperacao.usuario.id.eq(usuario.getId()).and(qOperacao.encerrada.isTrue()).and(qOperacao.dataInicio.between(inicioPeriodo, terminoPeriodo))).list(qOperacao);
    }
}
