package com.compulynx.compas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compulynx.compas.models.RightMaster;

@Repository
public interface UserRightsRepository extends JpaRepository<RightMaster, Long>{

	List<RightMaster> findAll();

}
