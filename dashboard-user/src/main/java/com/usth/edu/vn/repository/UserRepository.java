package com.usth.edu.vn.repository;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.UserDetails;
import com.usth.edu.vn.model.Users;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Date;
import java.util.Optional;

import static com.usth.edu.vn.exception.ExceptionType.INCORRECT_PASSWORD;
import static com.usth.edu.vn.exception.ExceptionType.USER_EXISTED;
import static com.usth.edu.vn.exception.ExceptionType.USER_NOT_FOUND;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {

  @Inject
  UserDetailsRepository userDetailsRepository;

  public Users getUserById(long id) {
    Users user = findById(id);
    Users tempUser = Users
        .builder()
        .username(user.getUsername())
        .userDetails(userDetailsRepository.getUserDetail(id))
        .build();
    return tempUser;
  }

  public Optional<Users> findByUsername(String username) {
    return streamAll().filter(user -> user.getUsername().equals(username))
      .findFirst();
  }

  public void addUser(Users user) throws CustomException {
    if (findByUsername(user.getUsername()).isPresent()) {
      throw new CustomException(USER_EXISTED);
    } else {
      UserDetails userDetails = user.getUserDetails();
      userDetails.setCreateTime(new Date());
      userDetails.setUser(user);
      user.setRoles("user");
      persist(user);
    }
  }

  // TODO: optimize update function
  public void updateUser(String username, String password, Users user) throws CustomException {
    if (findByUsername(username).isEmpty()) {
      throw new CustomException(USER_NOT_FOUND);
    } else {
      Users oldUser = findByUsername(username).get();
      UserDetails userDetails = userDetailsRepository.findById(oldUser.getUserDetails().getId());
      if (password != null) {
        if (oldUser.getPassword().equals(BcryptUtil.bcryptHash(password))) {
          oldUser.setPassword(password);
        } else {
          throw new CustomException(INCORRECT_PASSWORD);
        }
      }
      if (user.getRoles() != null) {
        oldUser.setRoles(user.getRoles());
      }
      String firstname = user.getUserDetails().getFirstname();
      if (firstname != null) {
        userDetails.setFirstname(firstname);
      }
      String lastname = user.getUserDetails().getLastname();
      if (lastname != null) {
        userDetails.setLastname(lastname);
      }
      String email = user.getUserDetails().getEmail();
      if (email != null) {
        userDetails.setEmail(email);
      }
      persist(oldUser);
      userDetailsRepository.persist(userDetails);
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
