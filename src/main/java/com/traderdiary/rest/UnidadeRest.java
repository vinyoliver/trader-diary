package com.traderdiary.rest;

import com.traderdiary.filter.SearchResultData;
import com.traderdiary.filter.UnidadeFilter;
import com.traderdiary.model.Unidade;
import com.traderdiary.service.UnidadeService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response.Status;

@Path("/unidade")
public class UnidadeRest extends BaseRest {

    @Inject
    private UnidadeService unidadeService;

    @POST
    @RolesAllowed("ROLE_EDITAR_UNIDADE")
    public Unidade create(Unidade entity) {
        return unidadeService.save(entity);
    }

    @PUT
    @Path("/update")
    @RolesAllowed("ROLE_EDITAR_UNIDADE")
    public Unidade update(Unidade entity) {
        return unidadeService.save(entity);
    }

    @POST
    @Path("/search")
    @Produces("application/json")
    public SearchResultData search(UnidadeFilter unidadeFilter) {
        return unidadeService.findByFilterPaginated(unidadeFilter);
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces("application/json")
    public Unidade findById(@PathParam("id") Long id) {
        Unidade entity = unidadeService.findById(Unidade.class, id);
        if (entity == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return entity;
    }

    @PUT
    @Path("/active")
    @RolesAllowed("ROLE_EDITAR_UNIDADE")
    public void active(Unidade entity) {
        unidadeService.active(entity);
    }

    @PUT
    @Path("/inactive")
    @RolesAllowed("ROLE_EDITAR_UNIDADE")
    public void inactive(Unidade entity) {
        unidadeService.inactive(entity);
    }

}
