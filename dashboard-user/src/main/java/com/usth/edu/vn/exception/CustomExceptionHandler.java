package com.usth.edu.vn.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<CustomException> {

    @Override
    public Response toResponse(CustomException e) {
        if (e.getMessage().equals(ExceptionType.USER_NOT_FOUND)) {
            return Response.status(NOT_FOUND)
                    .entity("User not found!")
                    .build();
        } else if (e.getMessage().equals(ExceptionType.USER_EXISTED)) {
            return Response.status(BAD_REQUEST)
                    .entity("User is already existed!")
                    .build();
        } else if (e.getMessage().equals(ExceptionType.INCORRECT_PASSWORD)){
            return Response.status(BAD_REQUEST)
                    .entity("Password is not matched!")
                    .build();
        } else {
            return Response.status(BAD_REQUEST)
                    .entity("What is this exception ???")
                    .build();
        }
    }
}
