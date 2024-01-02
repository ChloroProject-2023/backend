package com.usth.edu.vn.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InferenceDto {
  private long id;

  private long user_id;

  private String firstname;

  private String lastname;

  private long model_id;

  private long resource_id;

  private Date createTime;
}
