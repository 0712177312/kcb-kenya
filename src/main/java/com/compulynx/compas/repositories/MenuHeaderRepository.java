package com.compulynx.compas.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.compulynx.compas.models.MenuHeaderMaster;

/**
 * @author Mutwol
 *
 */

@Repository
public interface MenuHeaderRepository extends CrudRepository<MenuHeaderMaster, Long> {
	Collection<MenuHeaderMaster> findAll();
	
	@Query("SELECT c FROM MenuHeaderMaster c inner join fetch c.menus o "
			+ "inner join fetch o.groups g inner join g.rights s  "
			+ "where g.id=?1 and s.rightId=o.id order by c.headerPos asc")
	public List<MenuHeaderMaster> getAll(@Param("rightId") Long groupId);
}
