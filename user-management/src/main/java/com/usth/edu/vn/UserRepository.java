package com.usth.edu.vn;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {

    public List<Users> findByUsername(String username) {
        return list("SELECT u FROM User u WHERE u.username = ?1 ORDER BY" + "DESC", username);
    }
}
