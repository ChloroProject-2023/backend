package com.usth.edu.vn.resource;

import java.net.URI;
import java.util.List;

import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Ratings;
import com.usth.edu.vn.repository.ModelRepository;
import com.usth.edu.vn.repository.RatingRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

  @GET
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
  @Path("/search/{startWith}")
  @RolesAllowed({"admin", "user"})
  public Response getSearchModels (String startWith) {
    List<Models> allModels = modelRepository.searchModels(startWith);
    return Response.ok(allModels).build();
  }

  @GET
  @Path("{id}")
  @RolesAllowed({ "admin", "user" })
  public Response getModelById(long id) {
    Models model = modelRepository.getModelById(id);
    return Response.ok(model).build();
  }

  @GET
  @Path("/models-by-user")
  @RolesAllowed({ "admin", "user" })
  public Response getModelsByUser(@QueryParam("user_id") long user_id) {
    List<Models> allModels = modelRepository.findByUser(user_id);
    return Response.ok(allModels).build();
  }

  @GET
  @Path("/ratings/{model_id}")
  @RolesAllowed({ "admin", "user" })
  public Response getModelRatings(long model_id) {
    List<Ratings> allRatings = ratingRepository.getRatingByModel(model_id);
    return Response.ok(allRatings).build();
  }

  // Model CUD
  @POST
  @Path("/create")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response createModel(@QueryParam("user_id") long user_id, Models model) {
    modelRepository.addModel(user_id, model);
    if (modelRepository.isPersistent(model)) {
      return Response.created(URI.create("/new-model/" + model.getId())).build();
    }
    return Response.status(BAD_REQUEST).build();
  }

  @POST
  @Path("/update/{model_id}")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response updateModel(long model_id, Models model) {
    modelRepository.updateModel(model_id, model);
    return Response.created(URI.create("/update-model/" + model.getId())).build();
  }

  @DELETE
  @Path("/delete/{id}")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response deleteModel(long id) {
    modelRepository.deleteModel(id);
    return Response.ok().build();
  }
}
