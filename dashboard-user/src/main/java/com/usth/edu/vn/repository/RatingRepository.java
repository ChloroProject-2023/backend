package com.usth.edu.vn.repository;

import static com.usth.edu.vn.exception.ExceptionType.USER_NOT_FOUND;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Ratings;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.RatingDto;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class RatingRepository implements PanacheRepository<Ratings> {

  @Inject
  UserRepository userRepository;

  @Inject
  ModelRepository modelRepository;

  @Inject
  EntityManager entityManager;

  public List<RatingDto> findAllRatings() {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.RatingDto(
            r.id,
            r.stars,
            r.comment,
            u.id,
            u.username,
            ud.firstname,
            ud.lastname,
            m.id,
            r.createTime
        )
        FROM Ratings r
        INNER JOIN Users u
        ON u.id = r.user.id
        INNER JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        INNER JOIN Models m
        ON m.id = r.model.id
        """, RatingDto.class)
        .getResultList();
  }

  public RatingDto findRatingById(long id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.RatingDto(
            r.id,
            r.stars,
            r.comment,
            u.id,
            u.username,
            ud.firstname,
            ud.lastname,
            m.id,
            r.createTime
        )
        FROM Ratings r
        INNER JOIN Users u
        ON u.id = r.user.id
        INNER JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        INNER JOIN Models m
        ON m.id = r.model.id
        WHERE r.id = :id
        """, RatingDto.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public List<RatingDto> findRatingByModelId(long id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.RatingDto(
            r.id,
            r.stars,
            r.comment,
            u.id,
            u.username,
            ud.firstname,
            ud.lastname,
            m.id,
            r.createTime
        )
        FROM Ratings r
        INNER JOIN Users u
        ON u.id = r.user.id
        INNER JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        INNER JOIN Models m
        ON m.id = r.model.id
        WHERE m.id = :id
        """, RatingDto.class)
        .setParameter("id", id)
        .getResultList();
  }

  public List<RatingDto> findRatingByUserId(long id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.RatingDto(
            r.id,
            r.stars,
            r.comment,
            u.id,
            u.username,
            ud.firstname,
            ud.lastname,
            m.id,
            r.createTime
        )
        FROM Ratings r
        INNER JOIN Users u
        ON u.id = r.user.id
        INNER JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        INNER JOIN Models m
        ON m.id = r.model.id
        WHERE u.id = :id
        """, RatingDto.class)
        .setParameter("id", id)
        .getResultList();
  }

  public void addRating(long user_id, long model_id, Ratings rating) throws CustomException {
    Optional<Ratings> existedRating = entityManager.createQuery("""
        SELECT r
        FROM Ratings r
        WHERE r.user.id = :user_id
        AND r.model.id = :model_id
        """, Ratings.class)
        .setParameter("user_id", user_id)
        .setParameter("model_id", model_id)
        .getResultStream()
        .findAny();
    if (existedRating.isPresent()) {
      throw new CustomException("Already rated this model!");
    } else {
      Users user = userRepository.findById(user_id);
      Models model = modelRepository.findById(model_id);
      if (user == null) {
        throw new CustomException(USER_NOT_FOUND);
      } else if (model == null) {
        throw new CustomException("Model not found!");
      }
      rating.setUser(user);
      rating.setModel(model);
      rating.setCreateTime(new Date());
      persist(rating);
    }
  }

  public void updateRating(long user_id, long model_id, Ratings rating) throws CustomException {
    Optional<Ratings> existedRating = entityManager.createQuery("""
        SELECT r
        FROM Ratings r
        WHERE r.user.id = :user_id
        AND r.model.id = :model_id
        """, Ratings.class)
        .setParameter("user_id", user_id)
        .setParameter("model_id", model_id)
        .getResultStream()
        .findAny();
    if (existedRating.isEmpty()) {
      throw new CustomException("Have not rated this model before!");
    } else {
      Ratings saveRating = existedRating.get();
      if (rating.getStars() != null) {
        saveRating.setStars(rating.getStars());
      }
      if (rating.getComment() != null) {
        saveRating.setComment(rating.getComment());
      }
    }
  }

  public void deleteRating(long id) {
    deleteById(id);
  }
}
