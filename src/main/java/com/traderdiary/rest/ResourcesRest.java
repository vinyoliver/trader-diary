package com.traderdiary.rest;


import com.traderdiary.model.Cidade;
import com.traderdiary.model.Estado;
import com.traderdiary.model.UF;
import com.traderdiary.security.AppContext;
import com.traderdiary.service.CidadeService;
import com.traderdiary.service.EstadoService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;


@Path("/resources")
public class ResourcesRest extends BaseRest {

    @Inject
    private EstadoService estadoService;

    @Inject
    private CidadeService cidadeService;

    @Inject
    private AppContext appContext;

    @GET
    @Path("/estados")
    @Produces("application/json")
    public List<Estado> getEstados() {
        return estadoService.findAll();
    }

    @GET
    @Path("/cidades/{estadoUF}")
    @Produces("application/json")
    public List<Cidade> getCidades(@PathParam("estadoUF") UF estadoUF) {
        return cidadeService.findAllPorEstado(estadoUF);
    }

}