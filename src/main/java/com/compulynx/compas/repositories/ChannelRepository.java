package com.compulynx.compas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.compulynx.compas.models.Channel;
import com.compulynx.compas.models.extras.ChannelRep;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long>{
	
	@Query(nativeQuery = true, value = "SELECT ROWNUM AS COUNT,ID,CHANNELCODE,CHANNELNAME,(CASE STATUS WHEN 1 THEN 'true' ELSE 'false' END) AS STATUS,CREATED_BY AS "
			+ " CREATEDBY,CREATED_AT AS CREATEDON,(CASE WAIVED WHEN 'A' THEN 'Waived' else 'Normal' END) AS WAIVED,(CASE STATUS WHEN 1 THEN 'Active' ELSE 'InActive' END) AS ACTIVESTATUS FROM CHANNEL ")
	List<ChannelRep> getChannels();
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE channel set waived='W', waived_by=?1, waived_on=systimestamp WHERE id=?2 ")
	int waiveChannel(int waivedBy, Long id);
	
	@Query("select u from Channel u where u.waived='N' OR u.waived='R'")
	List<Channel> getChannelsToWaive();
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE channel set waived='A', waive_approved_by=?1, approvedwaive_on=systimestamp WHERE id=?2 ")
	int approveChannelWaive(int waivedApprovedBy, Long id);
	
	@Query("select u from Channel u where u.waived='W'")
	List<Channel> getWaivedChannles();

	@Modifying
	@Transactional
	@Query(nativeQuery=true, value="UPDATE channel set waived='R', waive_approved_by=?1, approvedwaive_on=systimestamp WHERE id=?2 ")
	int rejectChannel(int waivedApprovedBy, Long id);
}
