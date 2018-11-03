package com.traderdiary.rest;

import com.traderdiary.exception.AppException;
import com.traderdiary.filter.OperacaoFilter;
import com.traderdiary.filter.SearchResultData;
import com.traderdiary.model.*;
import com.traderdiary.security.AppContext;
import com.traderdiary.service.EmpresaService;
import com.traderdiary.service.ImagemOperacaoService;
import com.traderdiary.service.OperacaoService;
import com.traderdiary.service.UnidadeService;
import com.traderdiary.utils.FileUtils;
import com.traderdiary.wrapper.OperacaoWrapper;
import com.traderdiary.wrapper.ResumoWrapper;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.util.CollectionUtils;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Path("/operacao")
public class OperacaoRest extends BaseRest {

    @Inject
    private EmpresaService empresaService;

    @Inject
    private OperacaoService operacaoService;

    @Inject
    private UnidadeService unidadeService;

    @Inject
    private ImagemOperacaoService imagemOperacaoService;

    @Inject
    private AppContext appContext;

    @POST
    @RolesAllowed("ROLE_EDITAR_OPERACAO")
    public Operacao create(Operacao entity) {
        entity.setUsuario(appContext.getUsuarioLogado());
        return operacaoService.save(entity);
    }

    @PUT
    @Path("/update")
    @RolesAllowed("ROLE_EDITAR_OPERACAO")
    public Operacao update(Operacao entity) {
        final Operacao beforeUpdate = operacaoService.findById(entity.getId());
        appContext.podeAcessar(beforeUpdate.getUsuario());
        entity.setUsuario(appContext.getUsuarioLogado());
        return operacaoService.save(entity);
    }

    @POST
    @Path("/search")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_OPERACAO")
    public SearchResultData search(OperacaoFilter operacaoFilter) {
        operacaoFilter.setUsuario(appContext.getUsuarioLogado());
        final SearchResultData searchResultData = operacaoService.findByFilterPaginated(operacaoFilter);
        List<Operacao> operacoes = (List<Operacao>) searchResultData.getResultData();
        final List<OperacaoWrapper> collect = operacoes.stream().map(o -> new OperacaoWrapper(o)).collect(Collectors.toList());
        searchResultData.setResultData(collect);
        return searchResultData;
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_OPERACAO")
    public Operacao findById(@PathParam("id") Long id) {
        Operacao entity = operacaoService.findById(Operacao.class, id);
        appContext.podeAcessar(entity.getUsuario());
        if (entity == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return entity;
    }

    @POST
    @Path("/finalizar")
    @RolesAllowed("ROLE_EDITAR_OPERACAO")
    public Operacao finalizar(Operacao entity) {
        final Operacao beforeUpdate = operacaoService.findById(entity.getId());
        appContext.podeAcessar(beforeUpdate.getUsuario());
        entity.setUsuario(appContext.getUsuarioLogado());//TODO: Tratar usuario...
        operacaoService.finalizar(entity);
        return entity;
    }


    @GET
    @Path("next/{id:\\d+}")
    @Produces("text/plain")
    public long getNextOperation(@PathParam("id") Long id) {
        return operacaoService.next(id);
    }

    @GET
    @Path("previous/{id:\\d+}")
    @Produces("text/plain")
    public long getPreviousOperation(@PathParam("id") Long id) {
        return operacaoService.previous(id);
    }


    @GET
    @Path("/{id:\\d+}/unidades")
    @Produces("application/json")
    public List<Unidade> findUnidadesByEmpresa(@PathParam("id") Long id) {
        return unidadeService.findUnidadesByEmpresa(id);
    }

    @PUT
    @Path("/active/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_OPERACAO")
    public void active(@PathParam("id") Long id) {
        Empresa entity = empresaService.findById(id);
        empresaService.active(entity);
    }

    @DELETE
    @Path("/remove/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_OPERACAO")
    public void inactive(@PathParam("id") Long id) {
        Operacao entity = operacaoService.findById(id);
        appContext.podeAcessar(entity.getUsuario());
        operacaoService.remove(entity);
    }

    @POST
    @Path("/{id:\\d+}/{tipo}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ROLE_EDITAR_OPERACAO")
    public void uploadFile(MultipartFormDataInput uploadedInputStream, @PathParam("id") Long id, @PathParam("tipo") String tipo) {
        try {
            Operacao operacao = operacaoService.findById(id);
            List<InputPart> inputParts = uploadedInputStream.getFormDataMap().get("file");

            if (!CollectionUtils.isEmpty(inputParts)) {
                for (InputPart part : inputParts) {
                    String fileType = FileUtils.getFileType(part.getHeaders());

                    if ("ENTRADA".equals(tipo)) {
                        ImagemOperacao imagemOperacao = new ImagemOperacao("image.png", fileType, operacao, TipoImagem.ENTRADA);
                        imagemOperacaoService.uploadImagem(imagemOperacao, part.getBody(InputStream.class, null));
                    } else {
                        ImagemOperacao imagemOperacao = new ImagemOperacao("image.png", fileType, operacao, TipoImagem.SAIDA);
                        imagemOperacaoService.uploadImagem(imagemOperacao, part.getBody(InputStream.class, null));
                    }
                }
            }
        } catch (Exception e) {
            throw new AppException("error.upload.file");
        }
    }

    @GET
    @Path("/carregarGainLoss/{periodo}")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_OPERACAO")
    public ResumoWrapper carregarGaindAndLoss(@PathParam("periodo") String periodo) throws Exception {
        return operacaoService.countGainLoss(periodo);
    }


    @GET
    @Path("/carregarGainLossYear/{ano}")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_OPERACAO")
    public List<ResumoWrapper> carregarGainLossYear(@PathParam("ano") Integer ano) throws Exception {
        return operacaoService.countGainLossYear(ano);
    }


}