package com.usth.edu.vn.repository;

import com.usth.edu.vn.model.Resources;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ResourceRepository implements PanacheRepository<Resources> {

}
