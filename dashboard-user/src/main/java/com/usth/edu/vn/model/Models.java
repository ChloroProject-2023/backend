package com.usth.edu.vn.model;

import static jakarta.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "models", schema = "user_management")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Models {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String type;

  private String filepath;

  private String discription;

  @Temporal(TIMESTAMP)
  private Date createTime;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private Users users;

  @OneToOne(mappedBy = "models", cascade = CascadeType.PERSIST)
  private Inferences inferences;

  @OneToMany(mappedBy = "models", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Ratings> ratings;
}
