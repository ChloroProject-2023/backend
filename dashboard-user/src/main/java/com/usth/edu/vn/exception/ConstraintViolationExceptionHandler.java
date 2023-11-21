package com.usth.edu.vn.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return Response.status(BAD_REQUEST).entity(errors).build();
    }
}
