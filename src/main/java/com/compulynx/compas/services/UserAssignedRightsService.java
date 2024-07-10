package com.compulynx.compas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compulynx.compas.repositories.UserAssignedRightsRepository;

@Service
public class UserAssignedRightsService {
		@Autowired
		private UserAssignedRightsRepository userAssignedRightsRepository;

		public int removeAssignedRights(Long groupId) {
			// TODO Auto-generated method stub
			return userAssignedRightsRepository.removeGroup(groupId);
		}
}
