package com.compulynx.compas.repositories.roles_authorities;


import com.compulynx.compas.models.roles_authorities.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {
  AuthorityEntity findByName(String name);
}
