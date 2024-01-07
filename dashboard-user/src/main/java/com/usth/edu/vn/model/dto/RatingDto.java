package com.usth.edu.vn.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDto {

  private Long id;

  private Integer stars;

  private String comment;

  private Long user_id;

  private String username;

  private String lastname;

  private String firstname;

  private Long model_id;

  private Date createTime;
}
