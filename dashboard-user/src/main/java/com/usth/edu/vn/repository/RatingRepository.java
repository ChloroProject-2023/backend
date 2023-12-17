package com.usth.edu.vn.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Ratings;
import com.usth.edu.vn.model.Users;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RatingRepository implements PanacheRepository<Ratings> {

  @Inject
  UserRepository userRepository;

  @Inject
  ModelRepository modelRepository;

  public void addRating(long user_id, long model_id, Ratings rating) {
    Users user = userRepository.findById(user_id);
    Models models = modelRepository.findById(model_id);
    rating.setUsers(user);
    rating.setModels(models);
    rating.setCreateTime(new Date());
    persist(rating);
  }

  public void updateRating(long id, Ratings rating) {
    Ratings oldRating = findById(id);
    if (rating.getStars() != null) {
      oldRating.setStars(rating.getStars());
    }
    if (rating.getComment() != null) {
      oldRating.setComment(rating.getComment());
    }
    persist(oldRating);
  }

  public void deleteRating(long id) {
    deleteById(id);
  }

  public List<Ratings> getRatingByUser(long user_id) {
    Users user = userRepository.findById(user_id);
    List<Ratings> ratings = new ArrayList<>(user.getRatings());
    return ratings;
  }

  public List<Ratings> getRatignByModel(long model_id) {
    Models models = modelRepository.findById(model_id);
    List<Ratings> ratings = new ArrayList<>(models.getRatings());
    return ratings;
  }
}
