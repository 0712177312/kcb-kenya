package com.compulynx.compas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.extras.UserGroupRights;

@Repository
public interface UseGroupRepositories extends JpaRepository<UserGroup, Long> {
	List<UserGroup> findAll();

	Optional<UserGroup> findById(@Param("id") Long id);

	@Query(nativeQuery = true, value = "SELECT ID as rightId, RIGHTDISPLAYNAME as rightName,"
			+ " 'false' as allowView, 'false' as allowAdd, 'false' as allowEdit,"
			+ "  'false' as allowDelete FROM RIGHTSMASTER")
	List<UserGroupRights> getUserGroupRights();

//	@Query(nativeQuery=true, value="SELECT ID as rightId, RIGHTDISPLAYNAME as rightName,"
//			+ " 'false' as allowView, 'false' as allowAdd, 'false' as allowEdit,"
//			+ "  'false' as allowDelete FROM RIGHTSMASTER")

	@Query(nativeQuery = true, value = "SELECT UR.RIGHTID,RM.RIGHTDISPLAYNAME as rightName,  RM.RIGHTVIEWNAME as path, "
			+ "       (CASE UR.ALLOWVIEW WHEN 1 THEN 'true' else 'false' end) AS allowView, "
			+ "       (CASE UR.ALLOWADD WHEN 1 THEN 'true' else 'false' end) AS allowAdd, "
			+ "       (CASE UR.ALLOWEDIT WHEN 1 THEN 'true' else 'false' end) AS allowEdit, "
			+ "       (CASE UR.ALLOWDELETE WHEN 1 THEN 'true' else 'false' end) AS allowDelete "
			+ "        FROM USERGROUPSMASTER GR " + "        INNER JOIN USERASSIGNEDRIGHTS UR ON UR.GROUPID = GR.ID "
			+ "        RIGHT JOIN  RIGHTSMASTER RM ON RM.ID=UR.RIGHTID " + "        WHERE GR.ID=?1 "
			+ "        UNION ALL " + "        SELECT RM.ID,RM.RIGHTDISPLAYNAME as rightName,RM.RIGHTVIEWNAME as path, 'false' as allowView,  "
			+ "        'false' as allowAdd, 'false' as allowEdit,'false' as allowDelete "
			+ "          FROM USERGROUPSMASTER GM ,RIGHTSMASTER RM "
			+ "          WHERE GM.ID=?2 AND RM.ID NOT IN (SELECT ASR.RIGHTID  FROM USERASSIGNEDRIGHTS ASR WHERE ASR.GROUPID = ?3)")
	List<UserGroupRights> getUserGroups(Long id, Long id2, Long id3);

	// Get user group code by group id
	@Query(nativeQuery = true, value = "SELECT * FROM USERGROUPSMASTER WHERE ID =?1")
	UserGroup getRightCode(Long groupid);
}
