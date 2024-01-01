package com.usth.edu.vn.repository;

import java.util.Date;
import java.util.List;

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
            r.filePath,
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

  public ResourceDto findResourceByUserId(long user_id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.ResourceDto(
            r.id,
            r.filePath,
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
        .getSingleResult();
  }

  public List<ResourceDto> findAllResources() {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.ResourceDto(
            r.id,
            r.filePath,
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

  public void updateResource(long id, Resources resource) {
    findByIdOptional(id).map(r -> {
      r.setFilePath(resource.getFilePath());
      r.setType(resource.getType());
      return r;
    });
  }

  public void deleteResource(long id) {
    deleteById(id);
  }
}
