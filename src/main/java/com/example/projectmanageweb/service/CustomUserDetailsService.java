package com.example.projectmanageweb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.projectmanageweb.model.User;
import com.example.projectmanageweb.repository.UserRepository;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

@Service
public class CustomUserDetailsService implements UserDetailsService,UserService {
	
	private final UserRepository repo;

	public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }
	
	@Override
	public Optional<User> findByEmail(String email) {
		// TODO Auto-generated method stub
		return repo.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
        String role = u.getRole() != null && u.getRole().getRoleName() != null
                ? u.getRole().getRoleName().toUpperCase()
                : "MEMBER";  // fallback an toàn

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .authorities("ROLE_" + role)
                .build();
    }
	
	
	

}
