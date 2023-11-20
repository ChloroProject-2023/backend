package com.usth.edu.vn.repository;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.usth.edu.vn.model.UserDetails;
import com.usth.edu.vn.model.UserDetails$;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserDetailsRepository implements PanacheRepository<UserDetails> {

    @Inject
    JPAStreamer jpaStreamer;

    private static final int PAGE_SIZE = 20;

    public List<UserDetails> findAllUsers() {
        return jpaStreamer.stream(UserDetails.class)
                .toList();
    }

    public List<UserDetails> findPagingUsers(long pageNo) {
        return jpaStreamer.stream(UserDetails.class)
                .skip(pageNo * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .toList();
    }

    public List<UserDetails> searchUsers(String keyWord) {
        return jpaStreamer.stream(UserDetails.class)
                .toList()
                .stream()
                .filter(UserDetails$.firstname.startsWith(keyWord)
                        .or(UserDetails$.lastname.startsWith(keyWord))
                        .or(UserDetails$.email.startsWith(keyWord))
                        .or(userDetails$ -> userDetails$.getUsers().getUsername().startsWith(keyWord)))
                .sorted(UserDetails$.firstname
                        .thenComparing(UserDetails$.lastname.comparator()))
                .collect(Collectors.toList());
    }
}
