package com.traderdiary.rest;

import com.traderdiary.filter.NotificacaoFilter;
import com.traderdiary.filter.NotificacaoUsuarioFilter;
import com.traderdiary.filter.SearchResultData;
import com.traderdiary.model.Notificacao;
import com.traderdiary.model.StatusNotificacao;
import com.traderdiary.security.AppContext;
import com.traderdiary.service.NotificacaoService;
import com.traderdiary.wrapper.UsuarioWrapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/notificacao")
public class NotificacaoRest extends BaseRest {

    @Inject
    private AppContext appContext;

    @Inject
    private NotificacaoService notificacaoService;

    @POST
    @Consumes("application/json")
    @RolesAllowed({"ROLE_EDITAR_NOTIFICACAO"})
    public Notificacao create(Notificacao entity) {
        return notificacaoService.save(entity);
    }

    @GET
    @Path("/usuario/{id:\\d+}/number-notifications")
    @Produces("application/json")
    public long getQtdNotificacoes(@PathParam("id") Long usuarioId) {
        return notificacaoService.buscarQtdNotificoesNaoLidasPorUsuarioId(usuarioId);
    }

    @POST
    @Path("/searchRecebidas")
    @Produces("application/json")
    public SearchResultData getNotificacoesRecebidas(NotificacaoUsuarioFilter notificacaoFilter) {
        notificacaoFilter.setUsuario(appContext.getUsuarioLogado());
        notificacaoFilter.setUnidadeRecebidaId(appContext.getUnidadeSelecionada().getId());
        return notificacaoService.findByFilterPaginated(notificacaoFilter);
    }

    @POST
    @Path("/searchEnviadas")
    @Produces("application/json")
    @RolesAllowed({"ROLE_EDITAR_NOTIFICACAO"})
    public SearchResultData getNotificacoesEnviadas(NotificacaoFilter notificacaoFilter) {
        notificacaoFilter.setUsuario(appContext.getUsuarioLogado());
        return notificacaoService.findByFilterPaginated(notificacaoFilter);
    }


    @PUT
    @Path("/markAsRead/{id:\\d+}")
    @Produces("application/json")
    public void marcarNotificacaoComoLida(@PathParam("id") Long id) {
        notificacaoService.marcarComoLida(id);
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces("application/json")
    public Notificacao findById(@PathParam("id") Long id) {
        Notificacao entity = notificacaoService.findById(id);
        validarRegistro(entity);
        return entity;
    }

    @POST
    @Path("/enviarNotificacaoes")
    @RolesAllowed({"ROLE_EDITAR_NOTIFICACAO"})
    public void enviarNotificacoes(Notificacao notificacao, List<UsuarioWrapper> usuarios) {
        notificacaoService.gerarNotificacaoViaUsuario(notificacao, usuarios);
    }

    @POST
    @Path("/usuarios")
    @Produces("application/json")
    public SearchResultData getUsuarios(NotificacaoUsuarioFilter notificacaoFilter) {
        return notificacaoService.findByFilterPaginated(notificacaoFilter);
    }

    @PUT
    @Path("/finalizar")
    public void finalizar(Notificacao notificacao) {
        notificacao.setStatus(StatusNotificacao.FINALIZADA);
        notificacaoService.save(notificacao);
    }

}
