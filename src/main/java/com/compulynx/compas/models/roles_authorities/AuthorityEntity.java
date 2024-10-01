package com.compulynx.compas.models.roles_authorities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity(name = "authorities")
public class AuthorityEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  public AuthorityEntity(String name) {
    this.name = name;
  }

  public AuthorityEntity() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, length = 20)
  private String name;

  @ManyToMany(mappedBy = "authorities")
  private Collection<RoleEntity> roles;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<RoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Collection<RoleEntity> roles) {
    this.roles = roles;
  }
}
