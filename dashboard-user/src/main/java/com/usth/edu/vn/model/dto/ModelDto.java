package com.usth.edu.vn.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {

  private Long id;

  private String name;

  private String type;

  private String filepath;

  private String description;

  private long user_id;

  private String username;

  private String firstname;

  private String lastname;

  private Date createTime;
}
