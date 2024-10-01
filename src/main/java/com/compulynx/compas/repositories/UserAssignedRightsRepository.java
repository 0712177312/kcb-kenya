package com.compulynx.compas.repositories;


import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.compulynx.compas.models.UserAssignedRights;

@Repository
public interface UserAssignedRightsRepository extends JpaRepository<UserAssignedRights, Long> {
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value ="delete from userassignedrights WHERE GroupId=?1")
	int removeGroup(Long groupId);
}
