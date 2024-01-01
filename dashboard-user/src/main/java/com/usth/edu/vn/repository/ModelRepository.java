package com.usth.edu.vn.repository;

import java.util.Date;
import java.util.List;

import com.usth.edu.vn.model.Models;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.ModelDto;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

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
            SELECT new com.usth.edu.vn.model.dto.ModelDto(m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  m.user.id,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            INNER JOIN Users u
            ON u.id = m.user.id
            INNER JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            WHERE m.id = :id
            """, ModelDto.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public List<ModelDto> findModelsByUser(long user_id) {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  m.user.id,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            INNER JOIN Users u
            ON u.id = m.user.id
            INNER JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            WHERE u.id = :user_id
            """, ModelDto.class)
        .setParameter("user_id", user_id)
        .getResultList();
  }

  public List<ModelDto> findAllModels() {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  m.user.id,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            INNER JOIN Users u
            ON u.id = m.user.id
            INNER JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            """, ModelDto.class)
        .getResultList();
  }

  public List<ModelDto> findPagingModels(int pageNo) {
    return entityManager
        .createQuery("""
            SELECT new com.usth.edu.vn.model.dto.ModelDto(m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  m.user.id,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
            )
            FROM Models m
            INNER JOIN Users u
            ON u.id = m.user.id
            INNER JOIN UserDetails ud
            ON ud.id = u.userDetail.id
            """, ModelDto.class)
        .setFirstResult((pageNo - 1) * PAGE_SIZE)
        .setMaxResults(PAGE_SIZE)
        .getResultList();
  }

  public List<ModelDto> findMatchedModels(String keyword) {
    return entityManager
        .createQuery("""
              SELECT new com.usth.edu.vn.model.dto.ModelDto(m.id,
                  m.name,
                  m.type,
                  m.filepath,
                  m.description,
                  m.user.id,
                  ud.firstname,
                  ud.lastname,
                  m.createTime
              )
              FROM Models m
              INNER JOIN Users u
              ON u.id = m.user.id
              INNER JOIN UserDetails ud
              ON ud.id = u.userDetail.id
              WHERE m.name LIKE :name
              OR m.type LIKE :type
              OR m.description LIKE :description
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

  public void updateModel(long id, Models model) {
    findByIdOptional(id).map(m -> {
      m.setName(model.getName());
      m.setType(model.getType());
      m.setDescription(model.getDescription());
      return m;
    });
  }

  public void deleteModel(long id) {
    deleteById(id);
  }
}
