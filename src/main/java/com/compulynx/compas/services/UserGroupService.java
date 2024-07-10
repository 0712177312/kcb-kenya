package com.compulynx.compas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.extras.UserGroupRights;
import com.compulynx.compas.repositories.UseGroupRepositories;

@Service
public class UserGroupService {

	@Autowired
	private UseGroupRepositories useGroupRepositories;
	
	
	public List<UserGroup> userGroups() {
		return useGroupRepositories.findAll();
	}

	public Optional<UserGroup> getUserGroup(Long id) {
		// TODO Auto-generated method stub
		return useGroupRepositories.findById(id);
	}

	public UserGroup addGroup(UserGroup userGroup) {
		// TODO Auto-generated method stub
		return useGroupRepositories.save(userGroup);
	}

	public List<UserGroupRights> getUserGroupRights() {
		// TODO Auto-generated method stub
		return useGroupRepositories.getUserGroupRights();
	}

	public List<UserGroupRights> getGroupRights(Long id,Long id2, Long id3) {
		// TODO Auto-generated method stub
		return useGroupRepositories.getUserGroups(id,id2,id3);
	}
	public UserGroup getRightCode(Long groupid) {
		return useGroupRepositories.getRightCode(groupid);
	}
}
