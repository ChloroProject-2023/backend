package com.usth.edu.vn.resource;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.UserDetails;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.repository.UserDetailsRepository;
import com.usth.edu.vn.repository.UserRepository;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.usth.edu.vn.exception.ExceptionType.USER_NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.*;

@Path("/users")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    @Inject
    UserDetailsRepository userDetailsRepository;

    @GET
    @Path("{id}")
    @RolesAllowed({"admin", "user"})
    public Response getUserById(long id) {
      Users user = userRepository.getUserById(id);
      return Response.ok(user).build();
    }

    @GET
    @RolesAllowed({"admin", "user"})
    public Response getAllUsers() {
        List<Users> allUsers = userRepository.listAll();
        return Response.ok(allUsers).build();
    }

    @GET
    @Path("/paging/{pageNo}")
    @RolesAllowed({"admin", "user"})
    public Response getAllUsers(long pageNo) {
        List<UserDetails> allUsers = userDetailsRepository.findPagingUsers(pageNo);
        if(allUsers.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(allUsers).build();
    }

    @GET
    @Path("/search/{startsWith}")
    @RolesAllowed({"admin", "user"})
    public Response getSearchUsers( String startsWith) {
        List<UserDetails> anyUsers = userDetailsRepository.searchUsers(startsWith);
        if (anyUsers.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(anyUsers).build();
    }

    @GET
    @Path("/profile")
    @RolesAllowed({"admin", "user"})
    public Response getProfile (@QueryParam("username") String username) throws CustomException{
        Optional<Users> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(USER_NOT_FOUND);
        } else {
            return Response.ok(user.get()).build();
        }
    }

    @POST
    @Path("/create")
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
    @Path("/update")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response updateUser(@QueryParam("username") String username, @QueryParam("password") String password, Users user) throws CustomException {
        userRepository.updateUser(username, password, user);
        return Response.created(URI.create("/update-user/" + user.getId())).build();
    }
    
    @DELETE
    @Path("/delete")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response deleteUser(@QueryParam("username") String username) throws CustomException {
        userRepository.deleteUser(username);
        return Response.ok("User " + username +" is deleted!").build();
    }
}
