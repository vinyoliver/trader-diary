package com.traderdiary.filter;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.EntityPathBase;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.model.Unidade;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseFilter implements Serializable {

    private Integer page = 1;
    private Integer count = 10;

    @FilterField(name = "ativo", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    protected Boolean ativo;

    @FilterField(name = "unidade", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Unidade unidade;

    public Integer getFirstResult() {
        if (page != null && count != null) {
            return (page - 1) * count;
        }
        return null;
    }

    public abstract EntityPathBase getEntityPathBase();

    public OrderSpecifier[] getOrderBy() {
        return null;
    }

    public BooleanBuilder getCustomBuilder() {
        return null;
    }

}