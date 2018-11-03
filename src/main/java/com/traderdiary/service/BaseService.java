package com.traderdiary.service;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.util.ReflectionUtils;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.filter.BaseFilter;
import com.traderdiary.filter.SearchResultData;
import com.traderdiary.model.ModelBase;
import com.traderdiary.security.AppContext;
import com.traderdiary.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BaseService<T extends ModelBase> implements Serializable {

    @Inject
    private AppContext appContext;

    @PersistenceContext
    protected EntityManager em;

    protected void validarRegistro(ModelBase entity) {
        if (entity == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }

    @Transactional
    public T save(T model) {
        if (model.getId() == null) {
            em.persist(model);
        } else {
            model = em.merge(model);
        }
        return model;
    }

    @Transactional
    public void remove(T model) {
        model = em.merge(model);
        em.remove(model);
    }

    @Transactional
    public void active(T model) {
        model.setAtivo(true);
        em.merge(model);
    }

    @Transactional
    public void inactive(T model) {
        model.setAtivo(false);
        em.merge(model);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public T findById(Class<T> modelClass, Long id) {
        return em.find(modelClass, id);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public SearchResultData findByFilterPaginated(BaseFilter filter) {
        try {
            Long count = countByFilter(filter);
            List registros = findByFilter(filter);
            return new SearchResultData(count, registros);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public Long countByFilter(BaseFilter baseFilter) throws Exception {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(baseFilter.getEntityPathBase());
        from.where(generatePredicatesFromFilter(baseFilter));
        setCustomFilter(baseFilter, from);
        setPagination(baseFilter, from);
        return from.count();
    }

    @SuppressWarnings("unchecked")
    @Transactional(TxType.NOT_SUPPORTED)
    public List<ModelBase> findByFilter(BaseFilter baseFilter) throws Exception {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(baseFilter.getEntityPathBase());
        from.where(generatePredicatesFromFilter(baseFilter));
        setCustomFilter(baseFilter, from);
        setPagination(baseFilter, from);
        if (baseFilter.getOrderBy() != null) {
            from.orderBy(baseFilter.getOrderBy());
        }
        return from.list(baseFilter.getEntityPathBase());
    }

    private void setPagination(BaseFilter baseFilter, JPQLQuery from) {
        if (baseFilter.getCount() != null) {
            from.offset(baseFilter.getFirstResult());
            from.limit(baseFilter.getCount());
        }
    }

    private void setCustomFilter(BaseFilter baseFilter, JPQLQuery from) {
        final BooleanBuilder customBuilder = baseFilter.getCustomBuilder();
        if (customBuilder != null) {
            from.where(customBuilder);
        }
    }

    private static BooleanBuilder generatePredicatesFromFilter(BaseFilter filter) {
        Set<Field> allFields = ReflectionUtils.getFields(filter.getClass());
        BooleanBuilder where = new BooleanBuilder();
        for (Field field : allFields) {
            try {
                field.setAccessible(Boolean.TRUE);
                Object fieldValue = field.get(filter);
                boolean isCollection = Collection.class.isAssignableFrom(field.getType());
                if (fieldValue != null && (!isCollection || (isCollection && !CollectionUtils.isEmpty((Collection) fieldValue)))) {
                    if (fieldValue != null) {
                        FilterField filterFieldAnnotation = field.getAnnotation(FilterField.class);
                        boolean hasFilterFieldAnnotation = filterFieldAnnotation != null;
                        if (hasFilterFieldAnnotation) {
                            generatePredicateFromAnnotatedField(field, filterFieldAnnotation, fieldValue, where, filter);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("BaseService. Erro ao gerar Predicates");
            }
        }
        return where;
    }

    private static void generatePredicateFromAnnotatedField(Field field, FilterField filterFieldAnnotation,
                                                            Object fieldValue, BooleanBuilder where,
                                                            BaseFilter filter) {
        String fieldName = filterFieldAnnotation.name();
        FilterFieldPredicateType type = filterFieldAnnotation.type();
        FilterFieldOperatorType operator = filterFieldAnnotation.operator();
        Path path = Expressions.path(field.getType(), filter.getEntityPathBase(), fieldName);
        BooleanExpression predicate = null;

        if (fieldValue instanceof LocalDateTime) {
            if (filterFieldAnnotation.setFirstTimeOfDay()) {
                fieldValue = DateUtils.startOfDay((LocalDateTime) fieldValue);
            } else if (filterFieldAnnotation.setLastTimeOfDay()) {
                fieldValue = DateUtils.endOfDay((LocalDateTime) fieldValue);
            }
        }
        switch (type) {
            case EQUAL:
                predicate = Expressions.predicate(Ops.EQ, path, Expressions.constant(fieldValue));
                break;
            case LIKE:
                predicate = Expressions.predicate(Ops.LIKE, path, Expressions.constant(fieldValue.toString() + "%"));
                break;
            case GREATER_THAN_EQUAL:
                predicate = Expressions.predicate(Ops.GOE, path, Expressions.constant(fieldValue));
                break;
            case LESS_THAN_EQUAL:
                predicate = Expressions.predicate(Ops.LOE, path, Expressions.constant(fieldValue));
                break;
            case HAS:
                predicate = Expressions.predicate(Ops.IN, Expressions.constant(fieldValue), path);
                break;
            case IN:
                predicate = Expressions.predicate(Ops.IN, path, Expressions.constant(fieldValue));
                break;
            case STRING_CONTAINS_IC:
                String template = "FUNCTION('unaccent', {0})";
                predicate = Expressions.predicate(Ops.STRING_CONTAINS_IC,
                        Expressions.stringTemplate(template, path),
                        Expressions.stringTemplate(template, fieldValue.toString()));
                break;
            default:
                break;
        }
        if (operator == FilterFieldOperatorType.AND) {
            where.and(predicate);
        } else {
            where.or(predicate);
        }
    }


    public JPQLQuery instanceJPQLQuery() {
        return new JPAQuery(em);
    }
}
