package com.usth.edu.vn.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceDto {
  
  private long id;

  private String filePath;

  private String type;

  private long user_id;

  private String firstname;

  private String lastname;

  private Date createTime;
}
