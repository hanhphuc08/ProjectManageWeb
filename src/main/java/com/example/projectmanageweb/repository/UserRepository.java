package com.example.projectmanageweb.repository;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.model.User;

@Repository
public class UserRepository {
	private final JdbcTemplate jdbc;
	private UserRowMapper mapper = new UserRowMapper();
	
	public UserRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	
	public Optional<User> findByEmail(String email){
		
		var sql = """ 
					Select u.*, r.role_name 
					FROM users u left join roles r 
					ON u.role_id = r.role_id
					WHERE u.email = ? """;
		
		
		var list = jdbc.query(sql, mapper, email);
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}
	
	public boolean existsByEmail(String email) {
		final String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
	    if (email == null || email.isBlank()) {
	        return false;
	    }
	    Integer count = jdbc.queryForObject(sql, new Object[]{ email }, Integer.class);
	    return count != null && count > 0;
		
	}
	
	public Integer getRoleIdByName(String roleName) {

		return jdbc.query("Select role_id FROM roles where role_name = ?", (rs,rn) -> rs.getInt(1), roleName)
				.stream().findFirst().orElse(null);
	}
	
	public int save(User u) {
		  var sql = """
		  				INSERT INTO users(full_name, email, password_hash, role_id, phone_number)
		  				VALUES (?,?,?,?,?)
		  			""";
		  Integer roleId = (u.getRole()!=null) ? u.getRole().getRoleId() : null;
		  return jdbc.update(sql, u.getFullName(), u.getEmail(), u.getPasswordHash(), roleId, u.getPhoneNumber());
		}
	
	public int updatePasswordByEmail(String email, String newPasswordHash) {
		String sql = "UPDATE users SET password_hash = ? WHERE email = ?";
		return jdbc.update(sql, newPasswordHash, email);
	}

}
