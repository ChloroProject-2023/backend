package com.usth.edu.vn.resource;

import static jakarta.ws.rs.core.Response.Status.*;

import java.io.IOException;
import java.util.List;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.dto.InferenceDto;
import com.usth.edu.vn.repository.InferenceRepository;
import com.usth.edu.vn.services.InferenceService;

import jakarta.annotation.security.PermitAll;
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

  @Inject
  InferenceService inferenceService;

  @GET
  @RolesAllowed({ "admin", "user" })
  public Response getAllInferences() {
    List<InferenceDto> allInferences = inferenceRepository.findAllInferences();
    return Response.ok(allInferences).build();
  }

  @GET
  @Path("/{id}")
  @RolesAllowed({ "admin", "user" })
  public Response getInferenceById(long id) {
    InferenceDto inference = inferenceRepository.findInferenceById(id);
    return Response.ok(inference).build();
  }

  @GET
  @Path("/by-user")
  @RolesAllowed({ "admin", "user" })
  public Response getAllInferencesByUserId(@QueryParam("user_id") long user_id) {
    List<InferenceDto> allInferences = inferenceRepository.findAllInferencesByUserId(user_id);
    return Response.ok(allInferences).build();
  }

  @GET
  @Path("/by-model")
  @RolesAllowed({ "admin", "user" })
  public Response getAllInferencesByModelId(@QueryParam("model_id") long model_id) {
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
      @QueryParam("result") String result) throws CustomException {
    // Inferences inference = new Inferences();
    // inference.setResult(result);
    // inferenceRepository.addInference(user_id, model_id, resource_id, inference);
    // return Response.status(CREATED).entity("Inference " + inference.getId() + "
    // finished!").build();
    return Response.status(BAD_REQUEST).entity("Still working on this endpoints").build();
  }

  @POST
  @Path("/model-train-eval")
  @PermitAll
  @Transactional
  public Response trainEval(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      @QueryParam("resource_id") long resource_id,
      @QueryParam("pca_dim") int pca_dim) throws IOException, CustomException {
    return Response.status(BAD_REQUEST).entity("Still working on this endpoints!").build();
  }

  @GET
  @Path("/model-train-eval-MPAP")
  @PermitAll
  @Transactional
  public Response getResultTrainEval(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      @QueryParam("pca_dim") int pca_dim)
      throws IOException, CustomException {
    return Response.ok(inferenceService.trainEvalMPAP(user_id, pca_dim, model_id)).build();
  }

  @POST
  @Path("/model-inference-MPAP")
  @PermitAll
  @Transactional
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response getInference(
      @QueryParam("user_id") long user_id,
      @QueryParam("model_id") long model_id,
      @QueryParam("pca_dim") Integer pca_dim,
      @RestForm("test_sample") FileUpload input) throws IOException, CustomException {
    return Response.ok(inferenceService.inferenceMPAP(user_id, model_id, pca_dim, input)).build();
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
