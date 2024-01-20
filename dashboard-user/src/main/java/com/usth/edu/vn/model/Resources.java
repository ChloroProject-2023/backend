package com.usth.edu.vn.model;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resources", schema = "user_management")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Resources {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String filepath;

  private String type;

  private Date createTime;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @EqualsAndHashCode.Exclude
  private Users user;

  @OneToMany(mappedBy = "resource", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @EqualsAndHashCode.Exclude
  private Set<Inferences> inference;
}
