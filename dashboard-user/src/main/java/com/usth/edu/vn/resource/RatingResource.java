package com.usth.edu.vn.resource;

import java.net.URI;
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

@Path("/rating")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingResource {

  @Inject
  RatingRepository ratingRepository;

  @GET
  @Path("/ratings")
  @PermitAll
  public Response getAllRatings() {
    List<RatingDto> allRatings = ratingRepository.findAllRatings();
    return Response.ok(allRatings).build();
  }

  @GET
  @Path("/ratings-by-id/{id}")
  @PermitAll
  public Response getRatingById(long id) {
    RatingDto rating = ratingRepository.findRatingById(id);
    return Response.ok(rating).build();
  }

  @GET
  @Path("/ratings-by-model_id/{model_id}")
  @PermitAll
  public Response getRatingsByModelId(long model_id) {
    List<RatingDto> allRatings = ratingRepository.findRatingByModelId(model_id);
    return Response.ok(allRatings).build();
  }

  @GET
  @Path("/ratings-by-user_id/{user_id}")
  @PermitAll
  public Response getRatingsByUserId(long user_id) {
    List<RatingDto> allRatings = ratingRepository.findRatingByUserId(user_id);
    return Response.ok(allRatings).build();
  }

  @POST
  @Path("/ratings/create")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response createRating(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      Ratings rating) throws CustomException {
    ratingRepository.addRating(user_id, model_id, rating);
    return Response.created(URI.create("/user/" + user_id + "/model/" + model_id + "/new-rating/" + rating.getId()))
        .build();
  }

  @PUT
  @Path("/ratings/update")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response updateRating(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      Ratings rating) throws CustomException {
    ratingRepository.updateRating(user_id, model_id, rating);
    return Response.created(URI.create("/user/" + user_id + "/model/" + model_id + "/rating-update/" + rating.getId()))
        .entity(rating)
        .build();
  }

  @DELETE
  @Path("/ratings/delete/{id}")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response deleteRating(long id) {
    ratingRepository.deleteRating(id);
    return Response.ok("Rating " + id + " is deleted!").build();
  }
}
