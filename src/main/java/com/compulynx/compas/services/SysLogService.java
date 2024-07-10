package com.compulynx.compas.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compulynx.compas.models.reports.RptSysLogs;
import com.compulynx.compas.repositories.SysLogRepository;

@Service
public class SysLogService {
	@Autowired
	private SysLogRepository sysLogRepository;

	public int log(int userId, String activity,String loginType, String ipAddress) {
		// TODO Auto-generated method stub
		return sysLogRepository.sysLogRepository(userId, activity,loginType, ipAddress);
	}

	public List<RptSysLogs> getSysLogs() {
		// TODO Auto-generated method stub
		return sysLogRepository.getLogs();
	}

	public List<RptSysLogs> gtSystemLogs(Date fromDate, Date toDate,Long userId) {
		// TODO Auto-generated method stub
		return sysLogRepository.getSystemLogs(fromDate, toDate,userId);
	}
	public List<RptSysLogs> gtAllSystemLogs(Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return sysLogRepository.getAllSystemLogs(fromDate, toDate);
	}
	
}
