package com.usth.edu.vn.repository;

import java.util.Date;
import java.util.List;

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

  public Models getModelById(long id) {
    Models model = findById(id);
    Models tempModel = Models
      .builder()
      .name(model.getName())
      .type(model.getType())
      .user(userRepository.getUserById(model.getUser().getId()))
      .filepath(model.getFilepath())
      .description(model.getDescription())
      .createTime(model.getCreateTime())
      .build();
    return tempModel;
  }

  public void addModel(long user_id, Models model) {
    Users user = userRepository.findById(user_id);
    model.setUser(user);
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
    if (model.getDescription() != null) {
      oldModel.setDescription(model.getDescription());
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
      .toList();
  }

  public List<Models> findByUser(long user_id) {
    Users user = userRepository.findById(user_id);
    return user.getModels();
  }
}
