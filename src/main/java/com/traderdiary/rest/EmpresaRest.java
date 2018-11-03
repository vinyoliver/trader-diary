package com.traderdiary.rest;

import com.traderdiary.filter.EmpresaFilter;
import com.traderdiary.filter.SearchResultData;
import com.traderdiary.model.Empresa;
import com.traderdiary.model.Unidade;
import com.traderdiary.service.EmpresaService;
import com.traderdiary.service.UnidadeService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/empresa")
public class EmpresaRest extends BaseRest {

    @Inject
    private EmpresaService empresaService;

    @Inject
    private UnidadeService unidadeService;

    @POST
    @RolesAllowed("ROLE_EDITAR_EMPRESA")
    public Empresa create(Empresa entity) {
        return empresaService.save(entity);
    }

    @PUT
    @Path("/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_EMPRESA")
    public Empresa update(@PathParam("id") Long id, Empresa entity) {
        empresaService.save(entity);
        return entity;
    }

    @POST
    @Path("/search")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_EMPRESA")
    public SearchResultData search(EmpresaFilter empresaFilter) {
        return empresaService.findByFilterPaginated(empresaFilter);
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_EMPRESA")
    public Empresa findById(@PathParam("id") Long id) {
        Empresa entity = empresaService.findById(Empresa.class, id);
        if (entity == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return entity;
    }

    @GET
    @Path("/{id:\\d+}/unidades")
    @Produces("application/json")
    public List<Unidade> findUnidadesByEmpresa(@PathParam("id") Long id) {
        return unidadeService.findUnidadesByEmpresa(id);
    }

    @PUT
    @Path("/active/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_EMPRESA")
    public void active(@PathParam("id") Long id) {
        Empresa entity = empresaService.findById(id);
        empresaService.active(entity);
    }

    @PUT
    @Path("/inactive/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_EMPRESA")
    public void inactive(@PathParam("id") Long id) {
        Empresa entity = empresaService.findById(id);
        empresaService.inactive(entity);
    }

}