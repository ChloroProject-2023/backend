package com.usth.edu.vn.resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Resources;
import com.usth.edu.vn.model.dto.ResourceDto;
import com.usth.edu.vn.repository.ResourceRepository;
import com.usth.edu.vn.services.FileServices;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static com.usth.edu.vn.services.FileName.RESOURCES;
import static jakarta.ws.rs.core.Response.Status.*;

@Path("/resources")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Resource {

  @Inject
  ResourceRepository resourceRepository;

  @Inject
  FileServices fileServices;

  @GET
  @RolesAllowed({ "admin", "user" })
  public Response getAllResources() {
    List<ResourceDto> allResources = resourceRepository.findAllResources();
    return Response.ok(allResources).build();

  }

  @GET
  @Path("/{id}")
  @RolesAllowed({ "admin", "user" })
  public Response getResourceById(long id) {
    ResourceDto resourceDto = resourceRepository.findResourceById(id);
    return Response.ok(resourceDto).build();
  }

  @GET
  @Path("/user_id/{user_id}")
  @RolesAllowed({ "admin", "user" })
  public Response getResourceByUserId(long user_id) {
    List<ResourceDto> resourceDto = resourceRepository.findResourceByUserId(user_id);
    return Response.ok(resourceDto).build();
  }

  @GET
  @Path("/count")
  @RolesAllowed({ "admin", "user" })
  public Response getResourceCount() {
    long count = resourceRepository.count();
    return Response.ok(count).build();
  }

  @POST
  @Path("/create")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response createResource(
      @QueryParam("user_id") long user_id,
      @QueryParam("type") String type,
      @RestForm("resource") FileUpload input) throws IOException, CustomException {
    Resources resource = new Resources();
    resource.setType(type);
    resource.setFilepath(fileServices.uploadFile(user_id, input, RESOURCES + File.separator + type));
    resourceRepository.addResource(user_id, resource);
    if (resourceRepository.isPersistent(resource)) {
      return Response.created(URI.create("/user/" + user_id + "/new-resource/" + resource.getId())).build();
    } else {
      return Response.status(BAD_REQUEST).build();
    }
  }

  @PUT
  @Path("/update")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response updateResource(@QueryParam("id") long id, Resources resource) throws CustomException {
    resourceRepository.updateResource(id, resource);
    return Response.created(URI.create("/user/" + id + "/resource-update/" + resource.getId())).entity(resource)
        .build();
  }

  @DELETE
  @Path("/delete/{id}")
  @RolesAllowed({ "admin", "user" })
  @Transactional
  public Response deleteResource(long id) {
    fileServices.deleteDir(new File(resourceRepository.findResourceById(id).getFilepath()));
    resourceRepository.deleteResource(id);
    return Response.ok("Resource " + id + " is deleted!").build();
  }
}
