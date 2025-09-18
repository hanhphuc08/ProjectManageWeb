package com.example.projectmanageweb.service;

import java.util.Optional;

import com.example.projectmanageweb.model.User;

public interface UserService {
	
	Optional<User> findByEmail(String email);
}
