package com.compulynx.compas.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compulynx.compas.models.MenuHeaderMaster;
import com.compulynx.compas.repositories.MenuHeaderRepository;

@Service
public class MenuService {
	@Autowired
	private MenuHeaderRepository menuRepository;

	public Collection<MenuHeaderMaster> getMenuHeaders() {
		return menuRepository.findAll();
	}

	public List<MenuHeaderMaster> getGroupMenus(Long groupId) {
		// TODO Auto-generated method stub
		return menuRepository.getAll(groupId);
	}
	
	
}
