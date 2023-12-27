package com.usth.edu.vn.repository;

import com.usth.edu.vn.model.Users;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersRepository implements PanacheRepository<Users> {
}
