package com.usth.edu.vn;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/api/list")
@ApplicationScoped
public class UserResource {

    List<Users> users = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        return Response.ok(users).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUsers(Users newUser) {
        users.add(newUser);
        return Response.ok(users).build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteUsers(@PathParam("id")Long id) {
        users.stream()
                .filter(user ->
                        user.getId().equals(id))
                .findFirst()
                .ifPresent(user ->
                        users.remove(user));
        return Response.noContent().build();
    }
}
