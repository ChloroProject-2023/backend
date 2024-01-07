package com.usth.edu.vn.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.ModelDto;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@ApplicationScoped
public class ModelRepository implements PanacheRepository<Models> {

  @Inject
  UserRepository userRepository;

  @Inject
  EntityManager entityManager;

  private static final int PAGE_SIZE = 10;

  public Models getModelById(long id) {
    Models model = findById(id);
    return Models
        .builder()
        .name(model.getName())
        .type(model.getType())
        .user(userRepository.getUserById(model.getUser().getId()))
        .filepath(model.getFilepath())
        .description(model.getDescription())
        .createTime(model.getCreateTime())
        .build();
  }

  public ModelDto findModelById(long id) {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(
                  m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  AVG(r.stars),
                  m.user.id,
                  u.username,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            LEFT JOIN Users u
            ON u.id = m.user.id
            LEFT JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            LEFT JOIN Ratings r
            ON m.id = r.model.id
            LEFT JOIN Inferences i
            WHERE m.id = :id
            """, ModelDto.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public List<ModelDto> findModelsByUser(long user_id) {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(
                  m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  AVG(r.stars),
                  m.user.id,
                  u.username,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            LEFT JOIN Users u
            ON u.id = m.user.id
            LEFT JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            LEFT JOIN Ratings r
            ON m.id = r.model.id
            WHERE u.id = :user_id
            GROUP BY m.id
            """, ModelDto.class)
        .setParameter("user_id", user_id)
        .getResultList();
  }

  public List<ModelDto> findAllModels() {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(
                  m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  AVG(r.stars),
                  m.user.id,
                  u.username,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            LEFT JOIN Users u
            ON u.id = m.user.id
            LEFT JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            LEFT JOIN Ratings r
            ON m.id = r.model.id
            GROUP BY m.id
            """, ModelDto.class)
        .getResultList();
  }

  public List<ModelDto> findTopTenRecentlyUsedModels() {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(
                  m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  AVG(r.stars),
                  m.user.id,
                  u.username,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            LEFT JOIN Users u
            ON u.id = m.user.id
            LEFT JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            LEFT JOIN Ratings r
            ON m.id = r.model.id
            GROUP BY m.id
            ORDER BY m.createTime DESC
            LIMIT 10
            """, ModelDto.class)
        .getResultList();
  }

  public List<ModelDto> findTopTenMostUsedModels() {
    Query query = entityManager
        .createNativeQuery("""
            SELECT
                  m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  AVG(r.stars),
                  m.user_id,
                  u.username,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            FROM models m
            LEFT JOIN users u
            ON u.id = m.user_id
            LEFT JOIN user_details ud
            ON ud.user_id = u.id
            LEFT JOIN (
            SELECT model_id
            FROM inferences
            GROUP BY model_id
            ORDER BY COUNT(*) DESC)
            AS new_i
            ON new_i.model_id = m.id
            LEFT JOIN ratings r
            ON m.id = r.model_id
            GROUP BY m.id
            LIMIT 10
            """);
    List<Object[]> result = (List<Object[]>) query.getResultList();
    List<ModelDto> allModels = new ArrayList<>(result.size());
    for (Object[] o : result) {
      ModelDto model = new ModelDto();
      model.setId(Long.parseLong(o[0].toString()));
      model.setName(o[1].toString());
      model.setType(o[2].toString());
      model.setFilepath(o[3].toString());
      model.setDescription(o[4].toString());
      model.setStars(Double.parseDouble(o[5].toString()));
      model.setUser_id(Long.parseLong(o[6].toString()));
      model.setUsername(o[7].toString());
      model.setFirstname(o[8].toString());
      model.setLastname(o[9].toString());
      model.setCreateTime((Date) o[10]);
      allModels.add(model);
    }
    return allModels;
  }

  public List<ModelDto> findPagingModels(int pageNo, int pageSize) {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(
                  m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  AVG(r.stars),
                  m.user.id,
                  u.username,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            LEFT JOIN Users u
            ON u.id = m.user.id
            LEFT JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            LEFT JOIN Ratings r
            ON m.id = r.model.id
            GROUP BY m.id
            """, ModelDto.class)
        .setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize)
        .getResultList();
  }

  public List<ModelDto> findMatchedModels(String keyword) {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(
                m.id,
                m.name,
                m.type,
                m.filepath,
                m.description,
                AVG(r.stars),
                m.user.id,
                u.username,
                ud.firstname,
                ud.lastname,
                m.createTime
            )
            FROM Models m
            LEFT JOIN Users u
            ON u.id = m.user.id
            LEFT JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            LEFT JOIN Ratings r
            ON m.id = r.model.id
            WHERE m.name LIKE :name
            OR m.type LIKE :type
            OR m.description LIKE :description
            GROUP BY m.id
            """, ModelDto.class)
        .setParameter("name", "%" + keyword + "%")
        .setParameter("type", "%" + keyword + "%")
        .setParameter("description", "%" + keyword + "%")
        .getResultList();
  }

  public void addModel(long user_id, Models model) {
    Users user = userRepository.findById(user_id);
    model.setUser(user);
    model.setCreateTime(new Date());
    persist(model);
  }

  public void updateModel(long id, Models model) throws CustomException {
    Optional<Models> existedModel = findByIdOptional(id);
    if (existedModel.isEmpty()) {
      throw new CustomException("Model does not existed!");
    } else {
      Models saveModel = existedModel.get();
      if (model.getName() != null) {
        saveModel.setName(model.getName());
      }
      if (model.getType() != null) {
        saveModel.setType(model.getType());
      }
      if (model.getDescription() != null) {
        saveModel.setDescription(model.getDescription());
      }
    }
  }

  public void deleteModel(long id) {
    deleteById(id);
  }
}
