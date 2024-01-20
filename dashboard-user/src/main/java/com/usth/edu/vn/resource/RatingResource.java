package com.usth.edu.vn.resource;

import static jakarta.ws.rs.core.Response.Status.*;

import java.util.List;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Ratings;
import com.usth.edu.vn.model.dto.RatingDto;
import com.usth.edu.vn.repository.RatingRepository;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/ratings")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingResource {

  @Inject
  RatingRepository ratingRepository;

  @GET
  @Path("/")
  @PermitAll
  public Response getAllRatings() {
    List<RatingDto> allRatings = ratingRepository.findAllRatings();
    return Response.ok(allRatings).build();
  }

  @GET
  @Path("/{id}")
  @PermitAll
  public Response getRatingById(long id) {
    RatingDto rating = ratingRepository.findRatingById(id);
    return Response.ok(rating).build();
  }

  @GET
  @Path("/count")
  @PermitAll
  public Response getRatingCount() {
    return Response.ok(ratingRepository.count()).build();
  }

  @GET
  @Path("/model_id")
  @PermitAll
  public Response getRatingsByModelId(@QueryParam("model_id") long model_id) {
    List<RatingDto> allRatings = ratingRepository.findRatingByModelId(model_id);
    return Response.ok(allRatings).build();
  }

  @GET
  @Path("/user_id")
  @PermitAll
  public Response getRatingsByUserId(@QueryParam("user_id") long user_id) {
    List<RatingDto> allRatings = ratingRepository.findRatingByUserId(user_id);
    return Response.ok(allRatings).build();
  }

  @POST
  @Path("/create")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response createRating(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      Ratings rating) throws CustomException {
    ratingRepository.addRating(user_id, model_id, rating);
    return Response.status(CREATED).entity("User " + user_id + " rated model " + model_id + " rating " + rating.getId())
        .build();
  }

  @PUT
  @Path("/update")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response updateRating(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      Ratings rating) throws CustomException {
    ratingRepository.updateRating(user_id, model_id, rating);
    return Response.status(ACCEPTED)
        .entity("User " + user_id + " updated rating " + rating.getId() + " of model " + model_id).build();
  }

  @DELETE
  @Path("/delete/{id}")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response deleteRating(long id) {
    ratingRepository.deleteRating(id);
    return Response.ok("Rating " + id + " is deleted!").build();
  }
}
