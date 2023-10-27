package com.usth.edu.vn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Users {

    @Id
    @GeneratedValue
    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String password;

}
