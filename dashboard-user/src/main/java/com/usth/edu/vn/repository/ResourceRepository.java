package com.usth.edu.vn.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Resources;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.ResourceDto;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ResourceRepository implements PanacheRepository<Resources> {

  @Inject
  UserRepository userRepository;

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

  public void addResource(long user_id, Resources resource) {
    Users user = userRepository.findById(user_id);
    resource.setUser(user);
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
