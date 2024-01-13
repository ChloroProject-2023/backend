package com.usth.edu.vn.resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.dto.ModelDto;
import com.usth.edu.vn.repository.ModelRepository;
import com.usth.edu.vn.repository.RatingRepository;
import com.usth.edu.vn.services.FileServices;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static com.usth.edu.vn.services.FileName.MODELS;
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

  @Inject
  FileServices fileServices;

  @GET
  @Path("/")
  @PermitAll
  public Response getAllModels() {
    List<ModelDto> allModels = modelRepository.findAllModels();
    return Response.ok(allModels).build();
  }

  @GET
  @Path("{id}")
  @PermitAll
  public Response getModelById(long id) {
    ModelDto model = modelRepository.findModelById(id);
    return Response.ok(model).build();
  }

  @GET
  @Path("/paging")
  @PermitAll
  public Response getPagingModels(@QueryParam("pageNo") int pageNo, @QueryParam("pageSize") int pageSize) {
    List<ModelDto> allModels = modelRepository.findPagingModels(pageNo, pageSize);
    // if (allModels.isEmpty()) {
    // return Response.status(BAD_REQUEST).build();
    // }
    return Response.ok(allModels).build();
  }

  @GET
  @Path("/search/{keyword}")
  @PermitAll
  public Response getSearchModels(String keyword) {
    List<ModelDto> allModels = modelRepository.findMatchedModels(keyword);
    // if (allModels.isEmpty()) {
    // return Response.status(BAD_REQUEST).build();
    // }
    return Response.ok(allModels).build();
  }

  @GET
  @Path("/models-by-user/{user_id}")
  @PermitAll
  public Response getModelsByUser(long user_id) {
    List<ModelDto> allModels = modelRepository.findModelsByUser(user_id);
    return Response.ok(allModels).build();
  }

  @GET
  @Path("/top10-recently-use")
  @PermitAll
  public Response getTopTenRecentlyCreatedModels() {
    List<ModelDto> top10Models = modelRepository.findTopTenRecentlyCreatedModels();
    return Response.ok(top10Models).build();
  }

  @GET
  @Path("/top10-most-use")
  @PermitAll
  public Response getTopTenMostUsedModels() {
    List<ModelDto> top10Models = modelRepository.findTopTenMostUsedModels();
    return Response.ok(top10Models).build();
  }

  @GET
  @Path("/top10-best-rating")
  @PermitAll
  public Response getTopTenBestRatingModels() {
    List<ModelDto> top10Models = modelRepository.findTop10BestRatingModels();
    return Response.ok(top10Models).build();
  }

  @GET
  @Path("/count")
  @PermitAll
  public Response getModelCount() {
    long count = modelRepository.count();
    return Response.ok(count).build();
  }

  @POST
  @Path("/create")
  // @RolesAllowed({ "admin", "user" })
  @PermitAll
  @Transactional
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response createModel(
      @QueryParam("user_id") long user_id,
      @QueryParam("name") String name,
      @QueryParam("type") String type,
      @QueryParam("description") String description,
      @RestForm("model") FileUpload input)
      throws IOException {
    Models model = new Models();
    model.setName(name);
    model.setType(type);
    model.setDescription(description);
    model.setFilepath(fileServices.uploadFile(user_id, input, MODELS));
    modelRepository.addModel(user_id, model);
    if (modelRepository.isPersistent(model)) {
      return Response.created(URI.create("/user/" + user_id + "/new-model/" +
          model.getId())).build();
    }
    fileServices.uploadFile(user_id, input, MODELS);
    return Response.status(BAD_REQUEST).build();
  }

  @PUT
  @Path("/update/{model_id}")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response updateModel(long model_id, Models model) throws CustomException {
    modelRepository.updateModel(model_id, model);
    return Response.created(URI.create("/update-model/" + model_id)).entity(model).build();
  }

  @DELETE
  @Path("/delete/{id}")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response deleteModel(long id) {
    fileServices.deleteDir(new File(modelRepository.getModelById(id).getFilepath()));
    modelRepository.deleteModel(id);
    return Response.ok("Model" + id + " is deleted!").build();
  }
}
