package com.example.projectmanageweb.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.RegisterRequest;
import com.example.projectmanageweb.model.Role;
import com.example.projectmanageweb.model.User;
import com.example.projectmanageweb.repository.RoleRepository;
import com.example.projectmanageweb.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository repo;
	private final PasswordEncoder encoder;
	private final RoleRepository roleRepo;
	
	public AuthService(UserRepository repo, PasswordEncoder encoder, RoleRepository roleRepo) {
		super();
		this.repo = repo;
		this.encoder = encoder;
		this.roleRepo = roleRepo;
	}

	public void register(RegisterRequest req) {
		
		if(repo.existsByEmail(req.getEmail())) {
			throw new IllegalStateException("Email đã được sử dụng");
			
		}
		if(!req.getPassword().equals(req.getConfirmPassword())) {
			throw new IllegalStateException("Mật khẩu nhập lại không khớp");
		}
		
		Role memberRole = roleRepo.findByName("MEMBER")
		        .orElseThrow(() -> new IllegalStateException("Role MEMBER không tồn tại"));
		
		User u = new User();
		u.setFullName(req.getFullName());
		u.setEmail(req.getEmail());
		u.setPhoneNumber(req.getPhoneNumber());
		u.setPasswordHash(encoder.encode(req.getPassword()));
		u.setRoleId(memberRole.getRoleId());
		repo.save(u);
	}
	public List<User> findAll() {
        return repo.findAll();
    }
}
