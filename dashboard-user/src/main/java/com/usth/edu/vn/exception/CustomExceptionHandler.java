package com.usth.edu.vn.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.ws.rs.core.MediaType;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<CustomException> {

    @Override
    public Response toResponse(CustomException e) {
        if (e.getMessage().equals(ExceptionType.USER_NOT_FOUND)) {
            return Response.status(NOT_FOUND)
                    .entity("User not found!")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } else if (e.getMessage().equals(ExceptionType.USER_EXISTED)) {
            return Response.status(BAD_REQUEST)
                    .entity("User is already existed!")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } else if (e.getMessage().equals(ExceptionType.INCORRECT_PASSWORD)){
            return Response.status(BAD_REQUEST)
                    .entity("Password is not matched!")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } else {
            return Response.status(BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}
