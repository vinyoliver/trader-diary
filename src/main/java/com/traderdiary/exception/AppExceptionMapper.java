package com.traderdiary.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

	@Override
    public Response toResponse(AppException exception) {
		return Response.status(Response.Status.PRECONDITION_FAILED)
				.entity(exception.getMessages()).type("application/json")
				.build();
	}

}
