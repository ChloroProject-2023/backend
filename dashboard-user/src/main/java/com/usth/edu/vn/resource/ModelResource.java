package com.usth.edu.vn.resource;

import java.util.List;

import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Ratings;
import com.usth.edu.vn.repository.ModelRepository;
import com.usth.edu.vn.repository.RatingRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.*;

@Path("/models")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {

  @Inject
  ModelRepository modelRepository;

  @Inject
  RatingRepository ratingRepository;

  @GET
  @Path("/")
  @RolesAllowed({ "admin", "user" })
  public Response getAllModels() {
    List<Models> allModels = modelRepository.listAll();
    return Response.ok(allModels).build();
  }

  @Path("/paging/{pageNo}")
  @RolesAllowed({ "admin", "user" })
  public Response getAllModels(long pageNo) {
    List<Models> allModels = modelRepository.findPagingModels(pageNo);
    if (allModels.isEmpty()) {
      return Response.status(BAD_REQUEST).build();
    }
    return Response.ok(allModels).build();
  }

  @GET
  @Path("{id}")
  @RolesAllowed({ "admin", "user" })
  public Response getModelById(long id) {
    Models model = modelRepository.findById(id);
    return Response.ok(model).build();
  }

  @GET
  @Path("/models-by-user")
  public Response getModelsByUser(@QueryParam("user_id") long user_id) {
    List<Models> allModels = modelRepository.findByUser(user_id);
    return Response.ok(allModels).build();
  }

  @GET
  @Path("/ratings/{id}")
  public Response getRatingByModel(long id) {
    List<Ratings> allRatings = ratingRepository.getRatignByModel(id);
    return Response.ok(allRatings).build();
  }

  @DELETE
  @Path("/delete/{id}")
  public Response deleteModel(long id) {
    modelRepository.deleteModel(id);
    return Response.ok().build();
  }
}
