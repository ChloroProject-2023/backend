package com.usth.edu.vn;


import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.beans.Transient;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.NOT_FOUND;

@Path("/api/list")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    List<Users> users = new ArrayList<>();

    @GET
//    @PermitAll
    public Response getAllUsers() {
        List<Users> users = userRepository.listAll();
        return Response.ok(users).build();
    }

    @GET
    @Path("{id}")
    public Response getUserById(@PathParam("id")Long id){
        return userRepository.findByIdOptional(id)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(NOT_FOUND).build());
    }


    @GET
//    @RolesAllowed({"admin", "user"})
    @Path("name/{name}")
    public Response getByName(@PathParam("name")String name) {
        return userRepository.find("namee", name)
                .singleResultOptional()
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @Path("{id}")
//    @RolesAllowed("admin")
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

    @GET
    @Path("username/{username}")
    public Response getByUsername(@PathParam("username")String username) {
        List<Users> users = userRepository.findByUsername(username);
        return Response.ok(users).build();
    }

    @POST
    @Transactional
    public Response createUser(Users user) {
        userRepository.persist(user);
        if(userRepository.isPersistent(user)) {
            return Response.created(URI.create("/users/" + user.getId())).build();
        }
        return Response.status(BAD_REQUEST).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id")Long id) {
        boolean deleted = userRepository.deleteById(id);
        return deleted ? Response.noContent().build() :
                Response.status(NOT_FOUND).build();
    }
}
