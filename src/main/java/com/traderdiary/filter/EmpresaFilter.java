package com.traderdiary.filter;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.EntityPathBase;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.model.QEmpresa;
import lombok.Data;

@Data
public class EmpresaFilter extends BaseFilter {

    public static final OrderSpecifier[] ORDER_BY = new OrderSpecifier[]{QEmpresa.empresa.nome.asc()};

    @FilterField(name = "nome", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.STRING_CONTAINS_IC)
    private String nome;

    @Override
    public EntityPathBase getEntityPathBase() {
        return QEmpresa.empresa;
    }

    @Override
    public OrderSpecifier[] getOrderBy() {
        return ORDER_BY;
    }
}
