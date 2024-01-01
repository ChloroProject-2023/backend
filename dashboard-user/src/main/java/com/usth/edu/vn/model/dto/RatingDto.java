package com.usth.edu.vn.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDto {

  private long id;

  private Integer stars;

  private String comment;

  private long user_id;

  private String lastname;

  private String firstname;

  private long model_id;
}
