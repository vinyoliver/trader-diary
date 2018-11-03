package com.traderdiary.service;

import com.traderdiary.model.Cidade;
import com.traderdiary.model.QCidade;
import com.traderdiary.model.UF;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

@Stateless
public class CidadeService extends BaseService<Cidade> {

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Cidade> findAllPorEstado(UF estadoUF) {
        QCidade qCidade = QCidade.cidade;
        return instanceJPQLQuery().from(qCidade).where(qCidade.estado.uf.eq(estadoUF)).orderBy(qCidade.nome.asc())
                .list(qCidade);
    }

}
