package com.usth.edu.vn.model;

import java.util.Date;

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
@Table(name = "resources", schema = "models_management")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Resources {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String filePath;

  private String type;

  private Date createTime;

  @ManyToOne
  @JoinColumn(name = "resources_id", referencedColumnName = "id")
  private Users users;

  @OneToOne(mappedBy = "resources", cascade = CascadeType.ALL, orphanRemoval = true)
  private Inferences inferences;
}