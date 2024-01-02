package com.usth.edu.vn.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@ApplicationScoped
public class HomeResource {

  @GET
  @PermitAll
  @Produces(MediaType.TEXT_PLAIN)
  public Response home() {
    return Response.ok("This is home page. If you see only this text, frontend page is broken!").build();
  }
}
