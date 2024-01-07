package com.usth.edu.vn.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InferenceDto {
  private Long id;

  private String result;

  private Long user_id;

  private String firstname;

  private String lastname;

  private Long model_id;

  private Long resource_id;

  private Date createTime;
}
