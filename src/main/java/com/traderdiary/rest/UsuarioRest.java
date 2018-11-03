package com.traderdiary.rest;

import com.traderdiary.exception.AppException;
import com.traderdiary.filter.SearchResultData;
import com.traderdiary.filter.UsuarioFilter;
import com.traderdiary.model.Unidade;
import com.traderdiary.model.Usuario;
import com.traderdiary.security.AppContext;
import com.traderdiary.service.UsuarioService;
import com.traderdiary.wrapper.AlteraSenhaWrapper;
import com.traderdiary.wrapper.UsuarioLogadoWrapper;
import com.traderdiary.wrapper.UsuarioWrapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/usuario")
public class UsuarioRest extends BaseRest {

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private AppContext appContext;

    @POST
    @Path("/create")
    public Usuario create(Usuario entity) {
        entity = usuarioService.criarNovoUsuario(entity);
        return entity;
    }

    @PUT
    @Path("/update")
    @RolesAllowed("ROLE_EDITAR_USUARIO")
    public void update(Usuario entity) {
        usuarioService.save(entity);
    }

    @PUT
    @Path("/active/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_USUARIO")
    public void active(@PathParam("id") Long id) {
        Usuario entity = findById(id);
        usuarioService.active(entity);
    }

    @PUT
    @Path("/inactive/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_USUARIO")
    public void inactive(@PathParam("id") Long id) {
        Usuario entity = findById(id);
        usuarioService.inactive(entity);
    }

    @POST
    @Path("/search")
    @Produces("application/json")
    @RolesAllowed({"ROLE_CONSULTAR_USUARIO", "ROLE_REGISTRAR_ENTRADA_MATERIAL", "ROLE_REGISTRAR_SAIDA_MATERIAL"})
    public SearchResultData search(UsuarioFilter usuarioFilter) {
        usuarioFilter.setUnidadeSelecionada(appContext.getUnidadeSelecionada());
        return usuarioService.findByFilterPaginated(usuarioFilter);
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_USUARIO")
    public Usuario findById(@PathParam("id") Long id) {
        Usuario entity = usuarioService.findByIdAndUnidade(id, appContext.getUnidadeSelecionada());
        validarRegistro(entity);
        return entity;
    }

    @PUT
    @Path("/altera-senha")
    public void alterarSenha(AlteraSenhaWrapper dados) throws AppException {
        usuarioService.alterarSenha(dados);
    }

    @PUT
    @Path("/troca-unidade")
    public void trocarUnidade(Unidade unidade) throws AppException {
        appContext.setUnidadeSelecionada(unidade);
    }

    @GET
    @Path("/unidade-selecionada")
    @Produces("application/json")
    public Unidade getUnidadeSelecionada() {
        return appContext.getUnidadeSelecionada();
    }

    @GET
    @Path("/current-user")
    @Produces("application/json")
    public UsuarioLogadoWrapper getUsuarioLogado() {
        final Usuario usuarioLogado = appContext.getUsuarioLogado();
        if (usuarioLogado == null) {
            return null;
        } else {
            return new UsuarioLogadoWrapper(usuarioLogado, usuarioLogado.getAutoridades());
        }
    }

    @POST
    @Path("/buscar-usuarios-para-notificacao")
    @Produces("application/json")
    @RolesAllowed({"ROLE_CONSULTAR_USUARIO", "ROLE_EDITAR_NOTIFICACAO"})
    public Set<UsuarioWrapper> buscarUsuariosParaNotificacao(UsuarioFilter usuarioFilter) {
        usuarioFilter.setCount(null);
        List<Usuario> usuarios = (List<Usuario>) usuarioService.findByFilterPaginated(usuarioFilter).getResultData();
        return (Set<UsuarioWrapper>) usuarios.stream().map(u -> new UsuarioWrapper(u)).collect(Collectors.toSet());
    }

}
