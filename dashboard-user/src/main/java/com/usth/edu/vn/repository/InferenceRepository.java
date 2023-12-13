package com.usth.edu.vn.repository;

import java.util.Date;

import com.usth.edu.vn.model.Inferences;
import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Resources;
import com.usth.edu.vn.model.Users;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InferenceRepository implements PanacheRepository<Inferences> {

  @Inject
  UserRepository userRepository;

  @Inject
  ModelRepository modelRepository;

  @Inject
  ResourceRepository resourceRepository;

  public void addInference(long user_id, long model_id, long resource_id, Inferences inference) {
    Users user = userRepository.findById(model_id);
    Models model = modelRepository.findById(model_id);
    Resources resource = resourceRepository.findById(resource_id);
    inference.setUsers(user);
    inference.setModels(model);
    inference.setResources(resource);
    inference.setCreateTime(new Date());
    persist(inference);
  }
}
