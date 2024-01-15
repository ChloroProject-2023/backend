package com.usth.edu.vn.resource;

import static com.usth.edu.vn.services.FileName.*;

import java.io.File;
import java.io.IOException;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.services.FileServices;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/file")
@ApplicationScoped
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class FileResource {

  @Inject
  FileServices fileServices;

  @POST
  @Path("/upload-avatar")
  @PermitAll
  public Response uploadAvatar(@QueryParam("user_id") long user_id, @RestForm("image") FileUpload input)
      throws IOException {
    fileServices.deleteDir(user_id, AVATARS);
    return Response.ok(fileServices.uploadFile(user_id, input, AVATARS)).build();
  }

  @POST
  @Path("/upload-model")
  @PermitAll
  public Response uploadModel(@QueryParam("user_id") long user_id, MultipartFormDataInput input) throws IOException {
    return Response.ok(fileServices.uploadFile(user_id, input, MODELS)).build();
  }

  @POST
  @Path("/upload-resource")
  @PermitAll
  public Response uploadResource(@QueryParam("user_rd") long user_id, MultipartFormDataInput input) throws IOException {
    return Response.ok(fileServices.uploadFile(user_id, input, RESOURCES)).build();
  }

  @GET
  @Path("/get-avatar")
  @PermitAll
  @Produces("image/png")
  public Response getAvatar(@QueryParam("id") long user_id) throws IOException, CustomException {
    return Response.ok(fileServices.getAvatar(user_id)).build();
  }

  @GET
  @Path("/get-file")
  @PermitAll
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response getModel(@QueryParam("id") long user_id) throws IOException, CustomException {
    File modelFile = fileServices.getModel(user_id);
    ResponseBuilder response = Response.ok(modelFile);
    response.header("Content-Disposition", String.format("attachment; filename=\"%s\"", modelFile.getName()));
    return response.build();
  }

  @DELETE
  @Path("/delete-avatar")
  @PermitAll
  public Response deleteAvatar(@QueryParam("id") long user_id) {
    fileServices.deleteDir(user_id, AVATARS);
    return Response.ok().build();
  }
}
