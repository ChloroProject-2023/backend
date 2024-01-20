package com.usth.edu.vn.repository;

import static com.usth.edu.vn.exception.ExceptionType.USER_NOT_FOUND;

import java.util.Date;
import java.util.List;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Inferences;
import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Resources;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.InferenceDto;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class InferenceRepository implements PanacheRepository<Inferences> {

  @Inject
  UserRepository userRepository;

  @Inject
  ModelRepository modelRepository;

  @Inject
  ResourceRepository resourceRepository;

  @Inject
  EntityManager entityManager;

  public InferenceDto findInferenceById(long id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.InferenceDto(
            i.id,
            i.result,
            u.id,
            ud.firstname,
            ud.lastname,
            m.id,
            r.id,
            i.createTime
        )
        FROM Inferences i
        LEFT JOIN Users u
        ON u.id = i.user.id
        LEFT JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        LEFT JOIN Models m
        ON m.id = i.model.id
        LEFT JOIN Resources r
        ON r.id = i.resource.id
        WHERE i.id = :id
        """, InferenceDto.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public List<InferenceDto> findAllInferences() {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.InferenceDto(
            i.id,
            i.result,
            u.id,
            ud.firstname,
            ud.lastname,
            m.id,
            r.id,
            i.createTime
        )
        FROM Inferences i
        LEFT JOIN Users u
        ON u.id = i.user.id
        LEFT JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        LEFT JOIN Models m
        ON m.id = i.model.id
        LEFT JOIN Resources r
        ON r.id = i.resource.id
        """, InferenceDto.class)
        .getResultList();
  }

  public List<InferenceDto> findAllInferencesByUserId(long user_id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.InferenceDto(
            i.id,
            i.result,
            u.id,
            ud.firstname,
            ud.lastname,
            m.id,
            r.id,
            i.createTime
        )
        FROM Inferences i
        LEFT JOIN Users u
        ON u.id = i.user.id
        LEFT JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        LEFT JOIN Models m
        ON m.id = i.model.id
        LEFT JOIN Resources r
        ON r.id = i.resource.id
        WHERE u.id = :id
        """, InferenceDto.class)
        .setParameter("id", user_id)
        .getResultList();
  }

  public List<InferenceDto> findAllInferencesByModelId(long model_id) {
    return entityManager.createQuery("""
        SELECT NEW com.usth.edu.vn.model.dto.InferenceDto(
            i.id,
            i.result,
            u.id,
            ud.firstname,
            ud.lastname,
            m.id,
            r.id,
            i.createTime
        )
        FROM Inferences i
        LEFT JOIN Users u
        ON u.id = i.user.id
        LEFT JOIN UserDetails ud
        ON ud.id = u.userDetail.id
        LEFT JOIN Models m
        ON m.id = i.model.id
        LEFT JOIN Resources r
        ON r.id = i.resource.id
        WHERE m.id = :id
        """, InferenceDto.class)
        .setParameter("id", model_id)
        .getResultList();
  }

  public void addInference(long user_id, long model_id, long resource_id, Inferences inference) throws CustomException {
    Users user = userRepository.findById(user_id);
    Models model = modelRepository.findById(model_id);
    Resources resource = resourceRepository.findById(resource_id);
    if (user == null) {
      throw new CustomException(USER_NOT_FOUND);
    } else if (model == null) {
      throw new CustomException("Model not found!");
    } else if (resource == null) {
      throw new CustomException("Resource not found!");
    }
    inference.setUser(user);
    inference.setModel(model);
    inference.setResource(resource);
    inference.setCreateTime(new Date());
    persist(inference);
  }

  public void addInference(long user_id, long model_id, Inferences inference) throws CustomException {
    Users user = userRepository.findById(user_id);
    Models model = modelRepository.findById(model_id);
    if (user == null) {
      throw new CustomException(USER_NOT_FOUND);
    } else if (model == null) {
      throw new CustomException("Model not found!");
    }
    inference.setUser(user);
    inference.setModel(model);
    inference.setResource(null);
    inference.setCreateTime(new Date());
    persist(inference);
  }

  public void deleteInference(long id) {
    deleteById(id);
  }
}
