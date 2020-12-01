package com.project.passbook.gateway.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = User.TABLE_NAME)
public class User implements Serializable,Persistable {

  public static final String TABLE_NAME = "User";
  public static final String NAME_COLUMN = "name";
  public static final String PASSWORD_COLUMN = "password";
  public static final String USER_ROLE_COLUMN = "user_role";

  @Id
  @Column(name = NAME_COLUMN, nullable = false, unique = true)
  private String name;

  @Column(name = PASSWORD_COLUMN)
  private String password;

  @Column(name = USER_ROLE_COLUMN, nullable = false)
  private String role;

  @Override
  public Object getId() {
    return name;
  }

  @Override
  public boolean isNew() {
    return true;
  }
}