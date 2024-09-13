package com.compulynx.compas.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.compulynx.compas.models.extras.TellersToApproveDetach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.compulynx.compas.models.Teller;
import com.compulynx.compas.models.extras.TellerToApprove;
import com.compulynx.compas.models.extras.TellerToDisplay;

@Repository
public interface TellerRepository extends JpaRepository<Teller, Long>{

	@Query("select u from Teller u where u.tellerId=?1 and (u.verified='A' or u.verified='N' or u.verified='T' or u.verified='D')")
	Teller checkTeller(String tellerId);
	@Query("select u from Teller u where u.customerId=?1 and (u.verified='A' or u.verified='N' or u.verified='T' or u.verified='D')")
	Teller checkTellerCustomer(String customerId);
	
	@Query("select u from Teller u where u.tellerSignOnName=?1")
	Teller getTellerDetails(String tellr);
	
	@Query("select u from Teller u where u.verified='A' and u.departmentCode=?1")
	List<Teller> getBranchTellers(String branch);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value ="UPDATE tellermaster set verified='A',verifiedby=?1, verified_on=systimestamp  WHERE customerId=?2 and verified<>'D'")
	int approveTellers(int verifiedBy, String customerId);
	//Fetch all without filters
	@Query(nativeQuery = true, value ="SELECT ROWNUM AS COUNTER,customerId, " + 
			"CU.tellerName,cu.enroll_status as enroll, TO_CHAR(CU.CREATED_AT,'dd-mm-rrrr') AS ENROLLEDON,UM.FULLNAME AS CREATEDBY, UM.ID AS USERSID " + 
			"from tellermaster CU " + 
			"INNER JOIN USERMASTER UM ON UM.ID = CU.createdBy AND CU.VERIFIED = 'N'")
	List<TellerToApprove> getTellersToApproveAll();
	
	//Fetch with branch filter
	@Query(nativeQuery = true, value ="SELECT ROWNUM AS COUNTER,customerId, " + 
			"CU.tellerName,cu.enroll_status as enroll, TO_CHAR(CU.CREATED_AT,'dd-mm-rrrr') AS ENROLLEDON,UM.FULLNAME AS CREATEDBY, UM.ID AS USERSID " + 
			"from tellermaster CU " + 
			"INNER JOIN USERMASTER UM ON UM.ID = CU.createdBy AND CU.VERIFIED = 'N' AND UM.BRANCH=?1")
	List<TellerToApprove> getTellersToApprove(String branchCode);

	@Query(nativeQuery = true, value ="SELECT ROWNUM AS COUNTER,customerId, " +
			"CU.tellerName,TO_CHAR(CU.CREATED_AT,'dd-mm-rrrr') AS ENROLLEDON, CU.DELETED_BY AS DELETEDBY, UM.FULLNAME AS CREATEDBY, UM.ID AS USERSID " +
			"from tellermaster CU " +
			"INNER JOIN USERMASTER UM ON UM.ID = CU.createdBy AND CU.VERIFIED = 'D'")
	List<TellersToApproveDetach> getTellersToApproveDetach();

//	@Query("select u from Teller u where u.customerId=?1 OR tellerSignOnName=?2  AND u.enrollStatus<>'A'")
//	Teller getTellerToVerify(String customerId, String mnemonic);
//
//	@Modifying
//	@Transactional
//	@Query(nativeQuery=true, value="UPDATE tellermaster set ENROLL_STATUS='A' WHERE customerId=?1 ")
//	int updateTellerDetails(String customerId);
//
//	@Modifying
//	@Transactional
//	@Query(nativeQuery=true, value="DELETE teller_prints  WHERE profile=?1 ")
//	int removeTellerPrints(Long profileId);
//
//	@Modifying
//	@Transactional
//	@Query(nativeQuery=true, value="DELETE tellermaster  WHERE customerId=?1 ")
//	int removeTellerDetails(String customerId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value ="UPDATE tellermaster set verified='R', rejected_by=?1,reject_reason=?3, rejected_on=systimestamp WHERE customerId=?2")
	int rejectTellerApproval(int rejectedBy,String customerId,String reason);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value ="UPDATE tellermaster set verified='D',deleted_by=?1, deleted_on=systimestamp WHERE customerId=?2")
	int removeTeller(int deletedBy, String customerId);


	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="update tellermaster set verified='R' , enroll_status ='D' WHERE customerId=?1 AND verified='A'")
	int convertStaffToCustomer(String customerId);

	@Query("select u from Teller u where u.tellerId=?1 and verified='A'")
	Teller checkStaffApproved(String tellerId);

	@Query("select u from Teller u where u.tellerId=?1 and (verified='AD' or verified='R')")
	Teller checkStaffDeleted(String tellerId);

	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="update tellermaster set verified='N',createdby=?1, created_at=systimestamp WHERE tellerid=?2")
	int staffUnDelete(int createdBy, String tellerid);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update tellermaster set verified='AD' where customerId=?1")
	int approveRemoveTeller(String customerId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update tellermaster set verified='N', enroll_status ='U'  where customerId=?1")
	int rejectRemoveTeller(String customerId);

	@Query(nativeQuery=true,value="SELECT   T.DEPTCODE as branch,(select BRANCH from USERMASTER A where A.ID=T.CREATEDBY ) as branchCode,"
			+ "to_char(T.REJECTED_ON,'dd-mm-rrrr') AS rejectedOn,to_char(T.VERIFIED_ON,'dd-mm-rrrr') AS verifiedOn,"
			+ "to_char(T.CREATED_AT, 'dd-mm-rrrr') as createdOn,(SELECT TELLER from USERMASTER A where A.ID=T.CREATEDBY ) as createdBy,"
			+ "(SELECT TELLER from USERMASTER u where U.id=T.VERIFIEDBY ) as verifiedBy,T.TELLERNAME AS tellerName,T.TELLEREMAIL AS tellerEmail,"
			+ "(SELECT TELLER from USERMASTER A WHERE A.ID=T.REJECTED_BY ) as rejectedBy  "
			+ " from  TELLERMASTER T where T.CREATED_AT BETWEEN :fromDate AND :toDate AND T.VERIFIED=:enrolledType")
	List<TellerToDisplay> getEnrolledStaff(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("enrolledType") String enrolledType);

    @Query(nativeQuery=true,value="SELECT   T.DEPTCODE as branch,(select BRANCH from USERMASTER A where A.ID=T.CREATEDBY ) as branchCode,"
    		+ "to_char(T.REJECTED_ON,'dd-mm-rrrr') AS rejectedOn,to_char(T.VERIFIED_ON,'dd-mm-rrrr') AS verifiedOn,"
    		+ "to_char(T.CREATED_AT, 'dd-mm-rrrr') as createdOn,(SELECT TELLER from USERMASTER A where A.ID=T.CREATEDBY ) as createdBy,"
    		+ "(SELECT TELLER from USERMASTER u where U.id=T.VERIFIEDBY ) as verifiedBy,T.TELLERNAME AS tellerName,T.TELLEREMAIL AS tellerEmail,"
    		+ "(SELECT TELLER from USERMASTER A WHERE A.ID=T.REJECTED_BY ) as rejectedBy  "
    		+ "  from  TELLERMASTER T WHERE T.CREATED_AT BETWEEN :fromDate AND :toDate AND T.VERIFIED=:enrolledType AND T.DEPTCODE=:branchCode")
    List<TellerToDisplay> getEnrolledStaffByBranch(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("enrolledType") String enrolledType,
                                          @Param("branchCode") String branchCode);

	Optional<Teller> findByCustomerId(String customerId);
}
