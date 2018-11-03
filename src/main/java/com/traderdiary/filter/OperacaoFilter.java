package com.traderdiary.filter;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.EntityPathBase;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.model.Finalidade;
import com.traderdiary.model.QOperacao;
import com.traderdiary.model.Usuario;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Data
public class OperacaoFilter extends BaseFilter {

    public static final OrderSpecifier[] ORDER_BY = new OrderSpecifier[]{QOperacao.operacao.codigo.desc()};

    private String nomeAtivo;

    @FilterField(name = "finalidade", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Finalidade finalidade;

    @FilterField(name = "encerrada", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Boolean encerrada;

    @FilterField(name = "dataInicio", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.GREATER_THAN_EQUAL, setFirstTimeOfDay = true)
    private LocalDateTime dataInicioInicial;

    @FilterField(name = "dataInicio", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.LESS_THAN_EQUAL, setLastTimeOfDay = true)
    private LocalDateTime dataInicioFinal;

    @FilterField(name = "dataTermino", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.GREATER_THAN_EQUAL, setFirstTimeOfDay = true)
    private LocalDateTime dataTerminoInicial;

    @FilterField(name = "dataTermino", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.LESS_THAN_EQUAL, setLastTimeOfDay = true)
    private LocalDateTime dataTerminoFinal;

    @FilterField(name = "usuario", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Usuario usuario;

    @Override
    public EntityPathBase getEntityPathBase() {
        return QOperacao.operacao;
    }

    @Override
    public OrderSpecifier[] getOrderBy() {
        return ORDER_BY;
    }


    @Override
    public BooleanBuilder getCustomBuilder() {
        BooleanBuilder where = null;

        if (StringUtils.isNotEmpty(nomeAtivo)) {
            where = new BooleanBuilder();
            QOperacao qOperacao = QOperacao.operacao;
            final BooleanExpression eq = qOperacao.papel.containsIgnoreCase(nomeAtivo).or(qOperacao.papel.containsIgnoreCase(nomeAtivo));
            where.and(eq);
        }
        return where;
    }
}
