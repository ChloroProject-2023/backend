package com.usth.edu.vn.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Users;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ModelRepository implements PanacheRepository<Models> {

  @Inject
  UserRepository userRepository;

  private static final int PAGE_SIZE = 5;

  public void addModel(long user_id, Models model) {
    Users user = userRepository.findById(user_id);
    model.setUsers(user);
    model.setCreateTime(new Date());
    persist(model);
  }

  public void updateModel(long id, Models model) {
    Models oldModel = findById(id);
    if (model.getName() != null) {
      oldModel.setName(model.getName());
    }
    if (model.getType() != null) {
      oldModel.setType(model.getType());
    }
    if (model.getFilepath() != null) {
      oldModel.setFilepath(model.getFilepath());
    }
    if (model.getDiscription() != null) {
      oldModel.setDiscription(model.getDiscription());
    }
    persist(oldModel);
  }

  public void deleteModel(long id) {
    deleteById(id);
  }

  public List<Models> findPagingModels(long pageNo) {
    return streamAll()
        .skip(pageNo * PAGE_SIZE)
        .limit(PAGE_SIZE)
        .toList();
  }

  public List<Models> searchModels(String keyWord) {
    return streamAll().filter(model -> model.getName().startsWith(keyWord))
        .collect(Collectors.toList());
  }

  public List<Models> findByUser(long user_id) {
    Users user = userRepository.findById(user_id);
    return user.getModels();
  }
}
