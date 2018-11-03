package com.traderdiary.rest;

import com.traderdiary.model.ModelBase;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

@Consumes("application/json")
public class BaseRest {

    protected void validarRegistro(ModelBase entity) {
        if (entity == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }

}
