package com.usth.edu.vn.repository;
import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.UserDetails;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.model.Users$;

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
    JPAStreamer jpaStreamer;

    @Inject
    UserDetailsRepository userDetailsRepository;

    public Optional<Users> findByUsername(String username) {
        final StreamConfiguration<Users> anyUsers = StreamConfiguration.of(Users.class);
        return jpaStreamer.stream(anyUsers)
                .filter(Users$.username.equal(username))
                .findFirst();
    }

    public void addUser(Users user) throws CustomException {
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new CustomException(USER_EXISTED);
        } else {
            UserDetails userDetails = user.getUserDetails();
            userDetails.setCreateTime(new Date());

            user.setRoles("user");

            userDetails.setUsers(user);

            persist(user);
            userDetailsRepository.persist(userDetails);
        }
    }

    public void updateUser(String username, Users user) throws CustomException {
        findByUsername(username).map(
                u -> {
                  u.setRoles(user.getRoles());
                  u.getUserDetails().setFirstname(user.getUserDetails().getFirstname());
                  u.getUserDetails().setLastname(user.getUserDetails().getLastname());
                  u.getUserDetails().setEmail(user.getUserDetails().getEmail());
                  return u;
                }).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    public void updatePassword(long id ,String oldPassword, String newPassword) throws CustomException {
        Optional<Users> user = findByIdOptional(id);
        if (user.isPresent()) {
          if (BcryptUtil.matches(oldPassword, user.get().getPassword())) {
            user.get().setPassword(newPassword);
          } else throw new CustomException(INCORRECT_PASSWORD);
        } else throw new CustomException(USER_NOT_FOUND);
    }
    
    public void deleteUser(String username) throws CustomException {
        if (findByUsername(username).isEmpty()) {
            throw new CustomException(USER_NOT_FOUND);
        } else {
            Users user = findByUsername(username).get();
            userDetailsRepository.deleteById(user.getUserDetails().getId());
            deleteById(user.getId());
        }
    }
}
