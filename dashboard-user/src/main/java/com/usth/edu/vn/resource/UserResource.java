package com.usth.edu.vn.resource;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.UserDetails;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.repository.UserDetailsRepository;
import com.usth.edu.vn.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestQuery;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;

@Path("/")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource extends PanacheEntityBase {

    @Inject
    UserRepository userRepository;

    @Inject
    UserDetailsRepository userDetailsRepository;

    @GET
    @Path("hello")
    @RolesAllowed({"admin", "user"})
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Users";
    }

    @GET
    @Path("users")
    @RolesAllowed({"admin", "user"})
    public Response getAllUsers() {
        List<UserDetails> allUsers = userDetailsRepository.findAllUsers();
        return Response.ok(allUsers).build();
    }

    @GET
    @Path("users/paging/{pageNo}")
    @RolesAllowed({"admin", "user"})
    public Response getAllUsers(long pageNo) {
        List<UserDetails> allUsers = userDetailsRepository.findPagingUsers(pageNo);
        if(allUsers.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(allUsers).build();
    }

    @GET
    @Path("users/search/{startsWith}")
    @RolesAllowed({"admin", "user"})
    public Response getSearchUsers( String startsWith) {
        List<UserDetails> anyUsers = userDetailsRepository.searchUsers(startsWith);
        if (anyUsers.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(anyUsers).build();
    }

    @POST
    @Path("users")
    @PermitAll
    @Transactional
    public Response createUser(Users user) throws CustomException {
        userRepository.addUser(user);
        if (userRepository.isPersistent(user)) {
            return Response.created(URI.create("/new-user/" + user.getId())).build();
        }
        return Response.status(BAD_REQUEST).build();
    }

    @POST
    @Path("users/update")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response updateUser(@RestQuery String username, Users user) throws CustomException {
        userRepository.updateUser(username, user);
        if (userRepository.isPersistent(user)) {
            return Response.created(URI.create("/update-user/" + user.getId())).build();
        }
        return Response.status(BAD_REQUEST).entity("Blehh notthing is updated !!! HAHA (Still fixing)").build();
    }

}
