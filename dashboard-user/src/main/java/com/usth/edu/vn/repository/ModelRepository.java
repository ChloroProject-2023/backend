package com.usth.edu.vn.repository;

import com.usth.edu.vn.model.Models;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModelRepository implements PanacheRepository<Models> {

}
