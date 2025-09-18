package com.example.projectmanageweb.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.RegisterRequest;
import com.example.projectmanageweb.model.Role;
import com.example.projectmanageweb.model.User;
import com.example.projectmanageweb.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository repo;
	private final PasswordEncoder encoder;
	public AuthService(UserRepository repo, PasswordEncoder encoder) {
		super();
		this.repo = repo;
		this.encoder = encoder;
	}
	
	public void register(RegisterRequest req) {
		
		if(repo.existsByEmail(req.getEmail())) {
			throw new IllegalStateException("Email đã được sử dụng");
			
		}
		if(!req.getPassword().equals(req.getConfirmPassword())) {
			throw new IllegalStateException("Mật khẩu nhập lại không khớp");
		}
		
		Integer roleId = repo.getRoleIdByName("MEMBER");
		if(roleId == null) {
			throw new IllegalStateException("Hiện chưa có role này");
			
		}
		
		User u = new User();
		u.setFullName(req.getFullName());
        u.setEmail(req.getEmail());
        u.setPhoneNumber(req.getPhoneNumber());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        Role r = new Role();
        r.setRoleId(roleId);
        u.setRole(r);
        
        
        repo.save(u);
		
	}
}
