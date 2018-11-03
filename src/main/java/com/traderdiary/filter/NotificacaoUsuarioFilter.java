package com.traderdiary.filter;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.EntityPathBase;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.model.QNotificacaoUsuario;
import com.traderdiary.model.TipoNotificacao;
import com.traderdiary.model.Usuario;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacaoUsuarioFilter extends BaseFilter {

    public static final OrderSpecifier[] ORDER_BY = new OrderSpecifier[]{QNotificacaoUsuario.notificacaoUsuario.notificacao.dataEnvio.desc()};

    private Boolean lida;

    @FilterField(name = "usuario", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Usuario usuario;

    @FilterField(name = "notificacao.tipo", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.LIKE)
    private TipoNotificacao tipo;

    @FilterField(name = "notificacao.dataEnvio", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.GREATER_THAN_EQUAL)
    private LocalDateTime dataInicial;

    @FilterField(name = "notificacao.dataEnvio", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.LESS_THAN_EQUAL)
    private LocalDateTime dataFinal;

    private Long unidadeRecebidaId;

    @FilterField(name = "notificacao.id", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Long notificacaoId;

    @Override
    public EntityPathBase getEntityPathBase() {
        return QNotificacaoUsuario.notificacaoUsuario;
    }

    @Override
    public OrderSpecifier[] getOrderBy() {
        return ORDER_BY;
    }

    @Override
    public BooleanBuilder getCustomBuilder() {
        BooleanBuilder where = new BooleanBuilder();
        BooleanExpression eq;
        if (lida != null) {
            if (lida) {
                eq = QNotificacaoUsuario.notificacaoUsuario.dataLeitura.isNotNull();
            } else {
                eq = QNotificacaoUsuario.notificacaoUsuario.dataLeitura.isNull();
            }
            where.and(eq);
        }
        return where;
    }
}