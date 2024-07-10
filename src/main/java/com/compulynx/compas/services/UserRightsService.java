package com.compulynx.compas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compulynx.compas.models.RightMaster;
import com.compulynx.compas.repositories.UserRightsRepository;

@Service
public class UserRightsService {
	
	@Autowired
	private UserRightsRepository userRightsRepository;
	
	 public List<RightMaster> getRights() {
		 return userRightsRepository.findAll();
	 }
}
