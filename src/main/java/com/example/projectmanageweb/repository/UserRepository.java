package com.example.projectmanageweb.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.model.User;

@Repository
public class UserRepository {
	@Autowired JdbcTemplate jdbc;
	@Autowired UserRowMapper userRowMapper;
	
	
	public Optional<User> findByEmail(String email) {
	    String sql = """
	        SELECT u.*, r.role_name
	        FROM users u
	        JOIN roles r ON r.role_id = u.role_id
	        WHERE u.email = ?
	    """;

	    List<User> list = jdbc.query(sql, userRowMapper, email);
	    return list.stream().findFirst();
	}
	public User findById(int userId) {
	    String sql = """
	        SELECT u.*, r.role_name
	        FROM users u
	        JOIN roles r ON r.role_id = u.role_id
	        WHERE u.user_id = ?
	    """;

	    List<User> list = jdbc.query(sql, userRowMapper, userId);
	    return list.isEmpty() ? null : list.get(0);
	}
	 public boolean existsByEmail(String email) {
	        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE email=?", Integer.class, email);
	        return n != null && n > 0;
	    }
	
	public int save(User u) {
        String sql = """
            INSERT INTO users(full_name,email,password_hash,phone_number,avatar_url,role_id,created_at,updated_at)
            VALUES(?,?,?,?,?,?,NOW(),NOW())
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getPhoneNumber());
            ps.setString(5, u.getAvatarUrl());
            ps.setInt(6, u.getRoleId());
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        return key == null ? 0 : key.intValue();
        
    }


	public void updatePasswordByEmail(String email, String hashedPassword) {
		// TODO Auto-generated method stub
		
	}
	public List<User> findAll() {
        String sql = """
                SELECT user_id, full_name, email, role_id
                FROM users
                ORDER BY full_name
                """;
        return jdbc.query(sql, (rs, i) -> {
            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setFullName(rs.getString("full_name"));
            u.setEmail(rs.getString("email"));
            u.setRoleId(rs.getInt("role_id"));
            return u;
        });
    }
	
	

}
