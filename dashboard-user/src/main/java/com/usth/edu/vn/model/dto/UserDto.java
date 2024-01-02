package com.usth.edu.vn.model.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

  private Long id;

  private String username;

  private String password;

  private String role;

  private String firstname;

  private String lastname;

  private String email;

  private Date createTime;
}
