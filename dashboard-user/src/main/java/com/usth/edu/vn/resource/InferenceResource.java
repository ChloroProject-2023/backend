package com.usth.edu.vn.resource;

import java.net.URI;
import java.util.List;

import com.usth.edu.vn.model.Inferences;
import com.usth.edu.vn.model.dto.InferenceDto;
import com.usth.edu.vn.repository.InferenceRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/inferences")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InferenceResource {

  @Inject
  InferenceRepository inferenceRepository;

  @GET
  @RolesAllowed({ "admin", "user" })
  public Response getAllInferences() {
    List<InferenceDto> allInferences = inferenceRepository.findAllInferences();
    return Response.ok(allInferences).build();
  }

  @GET
  @Path("/by-user/{user_id}")
  @RolesAllowed({ "admin", "user" })
  public Response getAllInferencesByUserId(long user_id) {
    List<InferenceDto> allInferences = inferenceRepository.findAllInferencesByUserId(user_id);
    return Response.ok(allInferences).build();
  }

  @GET
  @Path("/by-model/{model_id}")
  @RolesAllowed({ "admin", "user" })
  public Response getAllInferencesByModelId(long model_id) {
    List<InferenceDto> allInferences = inferenceRepository.findAllInferencesByModelId(model_id);
    return Response.ok(allInferences).build();
  }

  @GET
  @Path("/count")
  @RolesAllowed({ "admin", "user" })
  public Response getInferenceCount() {
    long count = inferenceRepository.count();
    return Response.ok(count).build();
  }

  @POST
  @Path("/create")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response createInference(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      @QueryParam("resource_id") long resource_id,
      Inferences inference) {
    inferenceRepository.addInference(user_id, model_id, resource_id, inference);
    return Response.created(URI.create("/create-inference/" + inference.getId())).build();
  }

  @DELETE
  @Path("/delete/{id}")
  @Transactional
  @RolesAllowed({ "admin", "user" })
  public Response deleteInference(long id) {
    inferenceRepository.deleteInference(id);
    return Response.ok("Inference " + id + " is deleted!").build();
  }
}
