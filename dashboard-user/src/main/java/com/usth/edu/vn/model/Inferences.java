package com.usth.edu.vn.model;

import static jakarta.persistence.TemporalType.TIMESTAMP;

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
@Table(name = "inferences", schema = "models_management")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Inferences {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Temporal(TIMESTAMP)
  private Date createTime;

  private String result;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private Users users;

  @OneToOne
  @JoinColumn(name = "model_id", referencedColumnName = "id")
  private Models models;

  @OneToOne
  @JoinColumn(name = "resrource_id", referencedColumnName = "id")
  private Resources resources;
}
