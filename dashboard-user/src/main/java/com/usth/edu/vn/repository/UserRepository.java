package com.usth.edu.vn.repository;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.UserDetails;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.dto.UserDto;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.usth.edu.vn.exception.ExceptionType.INCORRECT_PASSWORD;
import static com.usth.edu.vn.exception.ExceptionType.USER_EXISTED;
import static com.usth.edu.vn.exception.ExceptionType.USER_NOT_FOUND;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {

  @Inject
  UserDetailsRepository userDetailsRepository;

  @Inject
  EntityManager entityManager;

  private static final int PAGE_SIZE = 20;

  public Users getUserById(long id) {
    Users user = findById(id);
    return Users
        .builder()
        .username(user.getUsername())
        .userDetail(userDetailsRepository.getUserDetail(id))
        .build();
  }

  public Optional<UserDto> findUserById(long id) {
    return entityManager
        .createQuery("""
            SELECT NEW com.usth.edu.vn.model.dto.UserDto(
                u.id,
                u.username,
                u.password,
                u.roles,
                ud.firstname,
                ud.lastname,
                ud.email,
                ud.createTime
            )
            FROM Users u
            INNER JOIN UserDetails ud
            ON u.id = ud.user.id
            WHERE u.id = :id
            """, UserDto.class)
        .setParameter("id", id)
        .getResultStream()
        .findAny();
  }

  public Optional<UserDto> findUserByUsername(String username) {
    return entityManager
        .createQuery("""
            SELECT NEW com.usth.edu.vn.model.dto.UserDto(
                u.id,
                u.username,
                u.password,
                u.roles,
                ud.firstname,
                ud.lastname,
                ud.email,
                ud.createTime
            )
            FROM Users u
            INNER JOIN UserDetails ud
            ON u.id = ud.user.id
            WHERE u.username = :username
            """, UserDto.class)
        .setParameter("username", username)
        .getResultStream()
        .findFirst();
  }

  public List<UserDto> findAllUsers() {
    return entityManager
        .createQuery("""
            SELECT NEW com.usth.edu.vn.model.dto.UserDto(
                u.id,
                u.username,
                u.password,
                u.roles,
                ud.firstname,
                ud.lastname,
                ud.email,
                ud.createTime
            )
            FROM Users u
            INNER JOIN UserDetails ud
            ON u.id = ud.user.id
            """,
            UserDto.class)
        .getResultList();
  }

  public List<UserDto> findPagingUsers(int pageNo, int pageSize) {
    return entityManager
        .createQuery("""
            SELECT NEW com.usth.edu.vn.model.dto.UserDto(
                u.id,
                u.username,
                u.password,
                u.roles,
                ud.firstname,
                ud.lastname,
                ud.email,
                ud.createTime
            )
            FROM Users u
            INNER JOIN UserDetails ud
            ON u.id = ud.user.id
            """,
            UserDto.class)
        .setFirstResult((pageNo - 1) * pageSize)
        .setMaxResults(pageSize)
        .getResultList();
  }

  public List<UserDto> findMatchedUsers(String keyword) {
    return entityManager
        .createQuery("""
              SELECT new com.usth.edu.vn.model.dto.UserDto(
                  u.id,
                  u.username,
                  u.password,
                  u.roles,
                  ud.firstname,
                  ud.lastname,
                  ud.email,
                  ud.createTime
              )
              FROM Users u
              INNER JOIN UserDetails ud
              ON u.id = ud.user.id
            WHERE u.username LIKE :username
            OR ud.firstname LIKE :firstname
            OR ud.lastname LIKE :lastname
            OR ud.email LIKE :email
            """, UserDto.class)
        .setParameter("username", "%" + keyword + "%")
        .setParameter("firstname", "%" + keyword + "%")
        .setParameter("lastname", "%" + keyword + "%")
        .setParameter("email", "%" + keyword + "%")
        .getResultList();
  }

  public Optional<Users> findByUsername(String username) {
    return streamAll().filter(user -> user
        .getUsername()
        .equals(username))
        .findFirst();
  }

  public void addUser(Users user) throws CustomException {
    if (findByUsername(user.getUsername()).isPresent()) {
      throw new CustomException(USER_EXISTED);
    } else {
      UserDetails userDetail = user.getUserDetail();
      userDetail.setCreateTime(new Date());
      userDetail.setUser(user);
      user.setRoles("user");
      persist(user);
    }
  }

  public void updateUser(long id, Users user) throws CustomException {
    Optional<Users> existedUser = findByIdOptional(id);
    if (existedUser.isEmpty()) {
      throw new CustomException(USER_NOT_FOUND);
    } else {
      Users saveUser = existedUser.get();
      if (user.getRoles() != null) {
        saveUser.setRoles(user.getRoles());
      }
      if (user.getUserDetail() != null) {
        String firstname = user.getUserDetail().getFirstname();
        if (firstname != null) {
          saveUser.getUserDetail().setFirstname(firstname);
        }
        String lastname = user.getUserDetail().getLastname();
        if (lastname != null) {
          saveUser.getUserDetail().setLastname(lastname);
        }
        String email = user.getUserDetail().getEmail();
        if (email != null) {
          saveUser.getUserDetail().setEmail(email);
        }
      }
    }
  }

  public void updatePassword(long id, String oldPassword, String newPassword) throws CustomException {
    Optional<Users> user = findByIdOptional(id);
    if (user.isPresent()) {
      if (BcryptUtil.matches(oldPassword, user.get().getPassword())) {
        user.get().setPassword(newPassword);
      } else
        throw new CustomException(INCORRECT_PASSWORD);
    } else
      throw new CustomException(USER_NOT_FOUND);
  }

  public void deleteUser(long id) throws CustomException {
    if (findByIdOptional(id).isEmpty()) {
      throw new CustomException(USER_NOT_FOUND);
    } else {
      delete(findById(id));
    }
  }

  public void deleteUser(String username) throws CustomException {
    if (findByUsername(username).isEmpty()) {
      throw new CustomException(USER_NOT_FOUND);
    } else {
      delete(findByUsername(username).get());
    }
  }
}
