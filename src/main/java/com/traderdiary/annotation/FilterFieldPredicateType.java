package com.traderdiary.annotation;

import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;

public enum FilterFieldPredicateType {
    EQUAL(Ops.EQ),
    LIKE(Ops.LIKE),
    GREATER_THAN_EQUAL(Ops.GT),
    LESS_THAN_EQUAL(Ops.LT),
    HAS(Ops.IN),
    IN(Ops.IN),
    STRING_CONTAINS_IC(Ops.STRING_CONTAINS_IC),
    IS_NOT_NULL(Ops.IS_NOT_NULL);

    private Operator<?> operator;

    FilterFieldPredicateType(Operator<?> operator) {
        this.operator = operator;
    }

    public Operator<?> getOperator() {
        return operator;
    }
}
