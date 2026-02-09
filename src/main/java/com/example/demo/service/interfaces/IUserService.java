package com.example.demo.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.dto.request.SignupDto;
import com.example.demo.dto.request.UserSearchDto;
import com.example.demo.entity.User;

public interface IUserService {
	User saveUser(SignupDto authSignupDto);
	User findByUsername(String username);
	User findById(Long id);
	Page<User> search(UserSearchDto searchDto, Pageable pageable);
}
