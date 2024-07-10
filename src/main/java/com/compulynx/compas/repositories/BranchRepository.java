package com.compulynx.compas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.compulynx.compas.models.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
	
	@Query("select u from Branch u where u.branchCode =?1 or u.branchName =?2")
	Branch checkIfBranchExists(String branchCode, String branchName);
	
	@Query("SELECT count(u) from Branch u")
	int getBranchCount();
	
	@Query("select u from Branch u where u.status=true")
	List<Branch> getActiveBranches();

	@Query("select u from Branch u where u.branchCode =?1")
	Branch getBranchToWaive(String branchCode);

	@Query("select u from Branch u where u.waived ='W'")
	List<Branch> getWaivedBranches();

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value="UPDATE branch set waived='W',waived_by=?1,waived_on=systimestamp  WHERE id=?2")
	int waiveBranch(int waivedBy, Long id);

	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE branch set waived='A', waive_approved_by=?1, approvedbranchwaive_on=systimestamp WHERE id=?2 ")
	int approveBranchWaive(int waivedApprovedBy, Long id);
	
	@Query("select u from Branch u where u.waived='N' OR u.waived='R'")
	List<Branch> getBranchesToWaive();
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE branch set waived='R', waive_approved_by=?1, approvedbranchwaive_on=systimestamp WHERE id=?2 ")
	int rejectBranchWaive(int waivedApprovedBy, Long id);
	
	@Query("SELECT u FROM Branch u where u.waived=:status")
	List<Branch> getBranchRpt(@Param("status") String status);
	
	@Query("SELECT count(u) from Branch u where u.waived='W' or u.waived='A'")
	int getWaivedBranchesCount();
	
	@Query("select u from Branch u where u.countryCode=?1")
	List<Branch> getActiveCountryBranches(String country);

}
