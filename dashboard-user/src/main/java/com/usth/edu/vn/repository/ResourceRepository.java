package com.usth.edu.vn.repository;

import static com.usth.edu.vn.exception.ExceptionType.USER_NOT_FOUND;
import static com.usth.edu.vn.services.FileName.RESOURCES;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Resources;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.ResourceDto;
import com.usth.edu.vn.services.FileServices;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ResourceRepository implements PanacheRepository<Resources> {

  @Inject
  UserRepository userRepository;

  @Inject
  FileServices fileServices;

  @Inject
  EntityManager entityManager;

  public ResourceDto findResourceById(long id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.ResourceDto(
            r.id,
            r.filepath,
            r.type,
            u.id,
            ud.firstname,
            ud.firstname,
            r.createTime
        )
        FROM Resources r
        INNER JOIN Users u
        ON u.id = r.user.id
        INNER JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        WHERE r.id = :id
        """, ResourceDto.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public List<ResourceDto> findResourceByUserId(long user_id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.ResourceDto(
            r.id,
            r.filepath,
            r.type,
            u.id,
            ud.firstname,
            ud.firstname,
            r.createTime
        )
        FROM Resources r
        INNER JOIN Users u
        ON u.id = r.user.id
        INNER JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        WHERE u.id = :id
        """, ResourceDto.class)
        .setParameter("id", user_id)
        .getResultList();
  }

  public List<ResourceDto> findAllResources() {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.ResourceDto(
            r.id,
            r.filepath,
            r.type,
            u.id,
            ud.firstname,
            ud.firstname,
            r.createTime
        )
        FROM Resources r
        INNER JOIN Users u
        ON u.id = r.user.id
        INNER JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        """, ResourceDto.class)
        .getResultList();
  }

  public void addResource(long user_id, Resources resource, FileUpload resourceFile)
      throws IOException, CustomException {
    Optional<Users> existedUser = userRepository.findByIdOptional(user_id);
    if (existedUser.isEmpty()) {
      throw new CustomException(USER_NOT_FOUND);
    }
    resource
        .setFilepath(fileServices.uploadFile(user_id, resourceFile, RESOURCES + File.separator + resource.getType()));
    resource.setUser(existedUser.get());
    resource.setCreateTime(new Date());
    persist(resource);
  }

  public void addResource(long user_id, Resources resource) throws CustomException {
    Optional<Users> existedUser = userRepository.findByIdOptional(user_id);
    if (existedUser.isEmpty()) {
      throw new CustomException(USER_NOT_FOUND);
    }
    resource.setUser(existedUser.get());
    resource.setCreateTime(new Date());
    persist(resource);
  }

  public void updateResource(long id, Resources resource) throws CustomException {
    Optional<Resources> existedResource = findByIdOptional(id);
    if (existedResource.isEmpty()) {
      throw new CustomException("Resource does not existed!");
    } else {
      Resources saveResource = existedResource.get();
      if (resource.getFilepath() != null) {
        saveResource.setFilepath(resource.getFilepath());
      }
      if (resource.getType() != null) {
        saveResource.setType(resource.getType());
      }
    }
  }

  public void deleteResource(long id) {
    deleteById(id);
  }
}
