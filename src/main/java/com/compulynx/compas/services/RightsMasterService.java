package com.compulynx.compas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compulynx.compas.models.RightMaster;
import com.compulynx.compas.repositories.RightsMasterRepository;

/**
 * @author Mutwol
 *
 */
@Service
public class RightsMasterService {

	 @Autowired
	 private RightsMasterRepository masterRepository;
	 
	 public List<RightMaster> getRights() {
		 return masterRepository.findAll();
	 }
}
