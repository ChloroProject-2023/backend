package com.usth.edu.vn.model;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
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
