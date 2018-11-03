package com.traderdiary.service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.traderdiary.model.Estado;
import com.traderdiary.model.QEstado;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

@Stateless
public class EstadoService extends BaseService<Estado> {

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Estado> findAll() {
        QEstado qEstado = QEstado.estado;
        final JPAQuery jpaQuery = new JPAQuery(em);
        return jpaQuery.from(QEstado.estado).orderBy(qEstado.uf.asc()).list(qEstado);
    }

}
