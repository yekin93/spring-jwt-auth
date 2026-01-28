package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepo;
import com.example.demo.service.interfaces.IRoleService;

@Service
public class RoleService implements IRoleService {
	
	private final RoleRepo roleRepo;
	
	public RoleService(RoleRepo roleRepo) {
		this.roleRepo = roleRepo;
	}

	@Override
	public List<Role> getAll() {
		
		return roleRepo.findAll();
	}

}
