package com.usth.edu.vn.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ratings", schema = "user_management")
public class Ratings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer stars;

  private String comment;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private Users users;

  @ManyToOne
  @JoinColumn(name = "model_id", referencedColumnName = "id")
  private Models models;
}
