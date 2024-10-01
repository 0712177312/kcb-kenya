package com.compulynx.compas.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.compulynx.compas.models.SysLogs;
import com.compulynx.compas.models.reports.RptSysLogs;

@Repository
public interface SysLogRepository extends JpaRepository<SysLogs, Long>{
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="INSERT INTO syslogs (USER_ID,SYSACTIVITY,LOGIN_TYPE,IP_ADDRESS,CREATED_AT) VALUES (?1, ?2, ?3,?4, systimestamp)")
	int sysLogRepository(int userId, String activity,String loginType, String ipAddress);
	
	@Query(nativeQuery=true, value="CALL GET_USERLOGS({})")
	List<RptSysLogs> getLogs();
	
	@Query(nativeQuery=true, value="SELECT UM.FULLNAME AS fullname, UM.USERNAME AS username, SY.SYSACTIVITY as activity,SY.LOGIN_TYPE as loginType, SY.IP_ADDRESS as ipAddress, SY.CREATED_AT as createdAt "
			+ " FROM SYSLOGS SY INNER JOIN USERMASTER UM ON UM.ID=SY.USER_ID"
			+ " WHERE SY.CREATED_AT BETWEEN :fromDate AND :toDate AND UM.ID=:userId ORDER BY SY.ID")
	List<RptSysLogs> getSystemLogs(Date fromDate, Date toDate,Long userId);

	@Query(nativeQuery=true, value="SELECT UM.FULLNAME AS fullname, UM.USERNAME AS username, SY.SYSACTIVITY as activity,SY.LOGIN_TYPE as loginType, SY.IP_ADDRESS as ipAddress, SY.CREATED_AT as createdAt "
			+ " FROM SYSLOGS SY INNER JOIN USERMASTER UM ON UM.ID=SY.USER_ID"
			+ " WHERE SY.CREATED_AT BETWEEN :fromDate AND :toDate ORDER BY SY.ID")
	List<RptSysLogs> getAllSystemLogs(Date fromDate, Date toDate);
}