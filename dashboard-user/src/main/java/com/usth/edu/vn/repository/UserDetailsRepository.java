package com.usth.edu.vn.repository;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.usth.edu.vn.model.UserDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class UserDetailsRepository implements PanacheRepository<UserDetails> {

    @Inject
    JPAStreamer jpaStreamer;

    @Inject
    UserRepository userRepository;

    private static final int PAGE_SIZE = 20;

    public List<UserDetails> findAllUsers() {
        return jpaStreamer.stream(UserDetails.class)
                .toList();
    }

    public UserDetails getUserDetail(long user_id) {
      UserDetails userDetail = userRepository.findById(user_id).getUserDetails();
      UserDetails tempUserDetail = UserDetails
        .builder()
        .firstname(userDetail.getFirstname())
        .lastname(userDetail.getLastname())
        .email(userDetail.getEmail())
        .createTime(userDetail.getCreateTime())
        .build();
      return tempUserDetail;
    }

    public List<UserDetails> findPagingUsers(long pageNo) {
        return jpaStreamer.stream(UserDetails.class)
                .skip(pageNo * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .toList();
    }

    public List<UserDetails> searchUsers(String keyWord) {
      return streamAll().filter(userDetail -> 
          userDetail.getFirstname().startsWith(keyWord) ||
          userDetail.getLastname().startsWith(keyWord) ||
          userDetail.getEmail().startsWith(keyWord) ||
          userDetail.getUser().getUsername().startsWith(keyWord))
        .sorted(Comparator.comparing(UserDetails::getFirstname).thenComparing(UserDetails::getLastname))
        .toList();
    }
}
