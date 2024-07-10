package com.compulynx.compas.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.compulynx.compas.models.Country;

@Repository
public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {
	
  @Query("select u from Country u order by u.createdOn desc")
  List<Country> findAll();
  
  @Query("select u from Country u where u.countryCode=?1 or u.name=?2")
  Country checkIfExists(String countryCode, String countryName );

	@Query("select count(u) from Country u")
	int getCountryCountry();
	
	@Query("select u from Country u where u.status=true")
	List<Country> getActiveCountries();
	
	@Query("select u from Country u where u.waived='N' OR u.waived='R'")
	List<Country> getCountriesToWaive();
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE country set waived='W', waived_by=?1, waived_on=systimestamp WHERE id=?2 ")
	int waiveCountry(int waivedBy, Long id);
	
	@Query("select u from Country u where u.waived='W'")
	List<Country> getWaivedWaived();
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE country set waived='A', waived_by=?1, waived_on=systimestamp WHERE id=?2 ")
	int approveCountryWaive(int waivedApprovedBy, Long id);

	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE country set waived='R', waived_by=?1, waived_on=systimestamp WHERE id=?2 ")
	int rejectCountryWaive(int waivedApprovedBy, Long id);
}
