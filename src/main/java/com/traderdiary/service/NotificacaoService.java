package com.traderdiary.service;

import com.traderdiary.model.*;
import com.traderdiary.security.AppContext;
import com.traderdiary.wrapper.UsuarioWrapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class NotificacaoService extends BaseService<Notificacao> {

    @Inject
    private AppContext appContext;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private PerfilService perfilService;

    @Inject
    private NotificacaoUsuarioService notificacaoUsuarioService;

    @Transactional
    public void gerarNotificacao(TipoNotificacao tipo, String mensagem, String... perfis) {
        List<Usuario> usuarios = new ArrayList<>();

        for (String nomePerfil : Arrays.asList(perfis)) {
            Perfil perfil = perfilService.findByName(nomePerfil);

            if (perfil != null) {
                usuarios.addAll(usuarioService.findByPerfis(Arrays.asList(perfil)));
            }
        }

        Notificacao notificacao = new Notificacao();
        notificacao.setTipo(tipo);
        notificacao.setMensagem(mensagem);
        save(notificacao);

        usuarios.forEach(usuario -> {
            NotificacaoUsuario notificacaoUsuario = new NotificacaoUsuario();
            notificacaoUsuario.setNotificacao(notificacao);
            notificacaoUsuario.setUsuario(usuario);
            notificacaoUsuarioService.save(notificacaoUsuario);
        });
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public long buscarQtdNotificoesNaoLidasPorUsuarioId(Long usuarioId) {
        QNotificacaoUsuario qNotificacaoUsuario = QNotificacaoUsuario.notificacaoUsuario;

        if (appContext.getUnidadeSelecionada() == null)
            return 0;

        return instanceJPQLQuery().from(qNotificacaoUsuario)
                .where(qNotificacaoUsuario.usuario.id.eq(usuarioId).and(qNotificacaoUsuario.dataLeitura.isNull())).count();
    }

    @Transactional
    public void marcarComoLida(Long idNotificacaoUsuario) {
        QNotificacaoUsuario qNotificacaoUsuario = QNotificacaoUsuario.notificacaoUsuario;

        NotificacaoUsuario notificacaoUsuario = instanceJPQLQuery().from(qNotificacaoUsuario)
                .where(qNotificacaoUsuario.id.eq(idNotificacaoUsuario)
                        .and(qNotificacaoUsuario.usuario.id.eq(appContext.getUsuarioLogado().getId())))
                .uniqueResult(qNotificacaoUsuario);

        if (notificacaoUsuario != null) {
            notificacaoUsuario.setDataLeitura(LocalDateTime.now());
            notificacaoUsuarioService.save(notificacaoUsuario);
        }
    }

    @Transactional
    public void gerarNotificacaoJob(TipoNotificacao tipo, String mensagem, Unidade unidade, String... perfis) {
        List<Usuario> usuarios = new ArrayList<>();

        for (String nomePerfil : Arrays.asList(perfis)) {
            Perfil perfil = perfilService.findByName(nomePerfil);

            if (perfil != null) {
                usuarios.addAll(usuarioService.findByPerfisEUnidade(Arrays.asList(perfil), unidade));
            }
        }

        Notificacao notificacao = new Notificacao();
        notificacao.setTipo(tipo);
        notificacao.setMensagem(mensagem);

        save(notificacao);

        usuarios.forEach(usuario -> {
            NotificacaoUsuario notificacaoUsuario = new NotificacaoUsuario();
            notificacaoUsuario.setNotificacao(notificacao);
            notificacaoUsuario.setUsuario(usuario);
            notificacaoUsuarioService.save(notificacaoUsuario);
        });
    }

    @Transactional
    public void gerarNotificacaoViaSistema(TipoNotificacao tipo, String mensagem, List<Usuario> usuarios) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipo(tipo);
        notificacao.setMensagem(mensagem);
        save(notificacao);

        usuarios.forEach(usuario -> {
            NotificacaoUsuario notificacaoUsuario = new NotificacaoUsuario();
            notificacaoUsuario.setNotificacao(notificacao);
            notificacaoUsuario.setUsuario(usuario);
            notificacaoUsuarioService.save(notificacaoUsuario);
        });
    }

    @Transactional(TxType.NOT_SUPPORTED)
    public Notificacao findById(Long id) {
        return findById(Notificacao.class, id);
    }

    public void gerarNotificacaoViaUsuario(Notificacao notificacao, List<UsuarioWrapper> usuarios) {
        save(notificacao);

        usuarios.forEach(usuario -> {
            NotificacaoUsuario notificacaoUsuario = new NotificacaoUsuario();
            notificacaoUsuario.setNotificacao(notificacao);
            notificacaoUsuario.setUsuario(usuarioService.findById(Usuario.class, usuario.getId()));
            notificacaoUsuarioService.save(notificacaoUsuario);
        });
    }

    @Override
    public Notificacao save(Notificacao model) {
        model.getNotificacaoUsuarios().stream().forEach(notificacaoUsuario -> notificacaoUsuario.setNotificacao(model));
        return super.save(model);
    }

}
