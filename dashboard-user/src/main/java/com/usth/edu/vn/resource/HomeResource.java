package com.usth.edu.vn.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
@ApplicationScoped
public class HomeResource {

  @GET
  @PermitAll
  public Response home() {
    return Response.ok().build();
  }
}
