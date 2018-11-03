package com.traderdiary.filter;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.EntityPathBase;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.model.QNotificacao;
import com.traderdiary.model.Usuario;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacaoFilter extends BaseFilter {

    public static final OrderSpecifier[] ORDER_BY = new OrderSpecifier[]{QNotificacao.notificacao.dataEnvio.desc()};

    @FilterField(name = "usuarioEnvio", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Usuario usuario;

    @FilterField(name = "dataEnvio", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.GREATER_THAN_EQUAL)
    private LocalDateTime dataInicial;

    @FilterField(name = "dataEnvio", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.LESS_THAN_EQUAL)
    private LocalDateTime dataFinal;

    @Override
    public EntityPathBase getEntityPathBase() {
        return QNotificacao.notificacao;
    }

    @Override
    public OrderSpecifier[] getOrderBy() {
        return ORDER_BY;
    }
}