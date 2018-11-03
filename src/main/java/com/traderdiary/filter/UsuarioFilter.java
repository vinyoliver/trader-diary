package com.traderdiary.filter;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.EntityPathBase;
import com.traderdiary.annotation.FilterField;
import com.traderdiary.annotation.FilterFieldOperatorType;
import com.traderdiary.annotation.FilterFieldPredicateType;
import com.traderdiary.model.Empresa;
import com.traderdiary.model.Perfil;
import com.traderdiary.model.QUsuario;
import com.traderdiary.model.Unidade;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class UsuarioFilter extends BaseFilter {

    public static final OrderSpecifier[] ORDER_BY = new OrderSpecifier[]{QUsuario.usuario.nome.asc()};

    private String nome;

    @FilterField(name = "cpf", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private String cpf;

    @FilterField(name = "perfil.id", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Long perfilId;

    @FilterField(name = "unidades", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.HAS)
    private Unidade unidadeSelecionada;

    @FilterField(name = "empresa", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Empresa empresa;

    @FilterField(name = "perfil", operator = FilterFieldOperatorType.AND, type = FilterFieldPredicateType.EQUAL)
    private Perfil perfil;

    @Override
    public EntityPathBase getEntityPathBase() {
        return QUsuario.usuario;
    }

    @Override
    public OrderSpecifier[] getOrderBy() {
        return ORDER_BY;
    }

    @Override
    public BooleanBuilder getCustomBuilder() {
        if (StringUtils.isEmpty(this.getNome())) {
            return null;
        }

        String template = "FUNCTION('unaccent', {0})";
        Path pathNome = Expressions.path(String.class, getEntityPathBase(), "nome");
        Path pathSobreNome = Expressions.path(String.class, getEntityPathBase(), "sobrenome");
        final BooleanExpression eq = Expressions.predicate(Ops.STRING_CONTAINS_IC,
                Expressions.stringTemplate(template, pathNome),
                Expressions.stringTemplate(template, this.getNome()))
                .or(
                        Expressions.predicate(Ops.STRING_CONTAINS_IC,
                                Expressions.stringTemplate(template, pathSobreNome),
                                Expressions.stringTemplate(template, this.getNome()))
                );

        BooleanBuilder where = new BooleanBuilder();
        where.and(eq);
        return where;
    }

}