package com.usth.edu.vn.resource;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.UserDto;
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

    @GET
    @Path("{id}")
    @RolesAllowed({"admin", "user"})
    public Response getUserById(long id) throws CustomException {
       Optional<UserDto> user = userRepository.findUserById(id);
        if (user.isEmpty()) {
            throw new CustomException(USER_NOT_FOUND);
        }
      return Response.ok(user).build();
    }

    @GET
    @RolesAllowed({"admin", "user"})
    public Response getAllUsers() {
        List<UserDto> allUsers = userRepository.findAllUsers();
        return Response.ok(allUsers).build();
    }

    @GET
    @Path("/paging/{pageNo}")
    @RolesAllowed({"admin", "user"})
    public Response getAllUsers(int pageNo) {
        List<UserDto> allUsers = userRepository.findPagingUsers(pageNo);
        if(allUsers.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(allUsers).build();
    }

    @GET
    @Path("/search/{keyword}")
    @RolesAllowed({"admin", "user"})
    public Response getSearchUsers( String keyword) {
        List<UserDto> anyUsers = userRepository.findMatchedUsers(keyword);
        if (anyUsers.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(anyUsers).build();
    }

    @GET
    @Path("/profile")
    @RolesAllowed({"admin", "user"})
    public Response getProfile (@QueryParam("username") String username) throws CustomException{
        Optional<UserDto> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(USER_NOT_FOUND);
        } else {
            return Response.ok(user.get()).build();
        }
    }

    @GET
    @Path("/count")
    @RolesAllowed({"admin", "user"})
    public Response getUsersCount() {
        long userCount = userRepository.count();
        return Response.ok(userCount).build();
    }

    @POST
    @Path("/create")
    @PermitAll
    @Transactional
    public Response createUser(Users user) throws CustomException {
        userRepository.addUser(user);
        if (userRepository.isPersistent(user)) {
            return Response.created(URI.create("/new-user/" + user.getId())).entity(user).build();
        }
        return Response.status(BAD_REQUEST).build();
    }

    @PUT
    @Path("/update")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response updateUser(@QueryParam("id") long id, Users user) throws CustomException {
        userRepository.updateUser(id, user);
        return Response.created(URI.create("/update-user/" + id)).entity(user).build();
    }

    @PUT
    @Path("/updatePassword")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response updatePassword(@QueryParam("id") long id, @QueryParam("oldPassword") String oldPassword, @QueryParam("newPassword") String newPassword) throws CustomException {
        userRepository.updatePassword(id, oldPassword, newPassword);
        return Response.created(URI.create("/update-password-user/" + id)).build();
  }
    
    @DELETE
    @Path("/delete")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response deleteUser(@QueryParam("username") String username) throws CustomException {
        userRepository.deleteUser(username);
        return Response.ok("User " + username +" is deleted!").build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response deleteUser(@PathParam("id") long id) throws CustomException {
      userRepository.deleteUser(id);
      return Response.ok("User " + id +" is deleted!").build();
    }
}
