package com.traderdiary.service;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.traderdiary.model.QUnidade;
import com.traderdiary.model.Unidade;
import com.traderdiary.security.AppContext;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

@Stateless
public class UnidadeService extends BaseService<Unidade> {

    @Inject
    private AppContext appContext;

    @Override
    @Transactional
    public Unidade save(Unidade unidade) {
        if (unidade.isNovo()) {
            unidade.setCodigo(gerarCodigo(unidade.getUsuario().getId()));
        }
        return super.save(unidade);
    }


    private Long gerarCodigo(Long empresaId) {
        QUnidade qUnidade = QUnidade.unidade;

        return instanceJPQLQuery().from(qUnidade)
                .where(qUnidade.usuario.id.eq(empresaId)).count() + 1L;
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public Unidade findByCnpj(String cnpj) {
//		QUnidade qUnidade = QUnidade.unidade;
//		return instanceJPQLQuery().from(qUnidade).where(qUnidade.cnpj.eq(cnpj)).uniqueResult(qUnidade);
        throw new NotImplementedException("Not use");
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Unidade> findUnidadesByEmpresa(Long id) {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(QUnidade.unidade);
        return from.where(QUnidade.unidade.usuario.id.eq(id)).list(QUnidade.unidade);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Unidade> findTodasUnidadesAtivas() {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(QUnidade.unidade);
        return from.where(QUnidade.unidade.ativo.isTrue()).list(QUnidade.unidade);
    }
}
