package com.traderdiary.rest;

import com.traderdiary.model.Perfil;
import com.traderdiary.service.PerfilService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/perfil")
public class PerfilRest extends BaseRest {

    @Inject
    private PerfilService perfilService;

    @POST
    @RolesAllowed("ROLE_EDITAR_PERFIL")
    public Response create(Perfil entity) {
        perfilService.save(entity);
        return Response.created(UriBuilder.fromResource(ResourcesRest.class).path(String.valueOf(entity.getId())).build())
                .build();
    }

    @DELETE
    @Path("/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_PERFIL")
    public Response deleteById(@PathParam("id") Long id) {
        Perfil entity = perfilService.findById(Perfil.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        perfilService.remove(entity);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id:\\d+}")
    @RolesAllowed("ROLE_EDITAR_PERFIL")
    public Response update(@PathParam("id") Long id, Perfil entity) {
        try {
            perfilService.save(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    @Produces("application/json")
    @RolesAllowed({"ROLE_CONSULTAR_PERFIL", "ROLE_CONSULTAR_USUARIO"})
    public List<Perfil> search() {
        return perfilService.findAllVisible();
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces("application/json")
    @RolesAllowed("ROLE_CONSULTAR_PERFIL")
    public Response findById(@PathParam("id") Long id) {
        Perfil entity;
        try {
            entity = perfilService.findById(Perfil.class, id);
        } catch (NoResultException nre) {
            entity = null;
        }
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }
}
