package com.usth.edu.vn.repository;

import java.util.Date;

import com.usth.edu.vn.model.Resources;
import com.usth.edu.vn.model.Users;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ResourceRepository implements PanacheRepository<Resources> {

  @Inject
  UserRepository userRepository;

  public void addResource(long user_id, Resources resource) {
    Users user = userRepository.findById(user_id);
    resource.setUsers(user);
    resource.setCreateTime(new Date());
    persist(resource);
  }

  public void updateResource(long id, Resources resource) {
    Resources oldResource = findById(id);
    if (resource.getFilePath() != null) {
      oldResource.setFilePath(resource.getFilePath());
    }
    if (resource.getType() != null) {
      oldResource.setType(resource.getType());
    }
    persist(oldResource);
  }

  public void deleteResource(long id) {
    deleteById(id);
  }
}
