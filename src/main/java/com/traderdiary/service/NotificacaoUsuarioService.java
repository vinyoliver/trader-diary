package com.traderdiary.service;

import com.traderdiary.model.NotificacaoUsuario;
import com.traderdiary.model.QNotificacaoUsuario;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.List;

@Stateless
public class NotificacaoUsuarioService extends BaseService<NotificacaoUsuario> {

    @Transactional(TxType.NOT_SUPPORTED)
    public List<NotificacaoUsuario> findAllPorNoficaocao(Long notificacaoId) {
        QNotificacaoUsuario qNotificacaoUsuario = QNotificacaoUsuario.notificacaoUsuario;

        return instanceJPQLQuery().from(qNotificacaoUsuario)
                .where(qNotificacaoUsuario.notificacao.id.eq(notificacaoId))
                .list(qNotificacaoUsuario);
    }

}
