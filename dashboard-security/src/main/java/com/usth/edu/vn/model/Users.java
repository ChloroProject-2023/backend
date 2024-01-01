package com.usth.edu.vn.model;

import io.quarkus.security.jpa.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users", schema = "user_management")
@UserDefinition
public class Users {

    @Id
    private Long id;

    @Username
    private String username;

    @Password
    private String password;

    @Roles
    private String roles;
}
