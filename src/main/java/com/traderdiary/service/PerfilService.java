package com.traderdiary.service;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.traderdiary.model.Perfil;
import com.traderdiary.model.QPerfil;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

@Stateless
public class PerfilService extends BaseService<Perfil> {

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Perfil> findAll() {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(QPerfil.perfil);
        return from.list(QPerfil.perfil);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public List<Perfil> findAllVisible() {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(QPerfil.perfil).where(QPerfil.perfil.visivel.isTrue());
        return from.list(QPerfil.perfil);
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public Perfil findByName(String name) {
        JPQLQuery query = new JPAQuery(em);
        final JPQLQuery from = query.from(QPerfil.perfil).where(QPerfil.perfil.nome.eq(name));
        return from.singleResult(QPerfil.perfil);
    }
}
