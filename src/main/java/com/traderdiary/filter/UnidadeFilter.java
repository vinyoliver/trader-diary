package com.traderdiary.filter;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.EntityPathBase;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.model.QUnidade;
import lombok.Data;

@Data
public class UnidadeFilter extends BaseFilter {

    public static final OrderSpecifier[] ORDER_BY = new OrderSpecifier[]{QUnidade.unidade.nome.asc()};

    @FilterField(name = "nome", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.LIKE)
    private String nome;

    @FilterField(name = "ativo", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Boolean ativo = true;

    @FilterField(name = "empresa.id", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Long empresaId;

    @Override
    public EntityPathBase getEntityPathBase() {
        return QUnidade.unidade;
    }

    @Override
    public OrderSpecifier[] getOrderBy() {
        return ORDER_BY;
    }


}
