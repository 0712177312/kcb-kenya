package com.compulynx.compas.repositories.roles_authorities;

import com.compulynx.compas.models.roles_authorities.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
  RoleEntity findByName(String name);
}
