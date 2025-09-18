package com.example.projectmanageweb.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.model.Role;
import com.example.projectmanageweb.model.User;

public class UserRowMapper implements RowMapper<User> {
	
	public User mapRow(ResultSet rs, int rowNum) throws SQLException{
		User u = new User();
		u.setUserId(rs.getInt("user_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setAvatarUrl(rs.getString("avatar_url"));
        u.setPhoneNumber(rs.getString("phone_number"));
        u.setWorkMode(rs.getString("work_mode"));
        u.setWorkHoursPerWeek((Integer) rs.getObject("work_hours_per_week"));
		
		Role r = new Role();
		r.setRoleId((int) rs.getObject("role_id"));
		r.setRoleName(rs.getString("role_name"));
		
		
		u.setRole(r);
		
		var cAt = rs.getTimestamp("created_at");
        var uAt = rs.getTimestamp("updated_at");
        if (cAt != null) u.setCreatedAt(cAt.toInstant());
        if (uAt != null) u.setUpdatedAt(uAt.toInstant());
		
		return u;
	}

}
