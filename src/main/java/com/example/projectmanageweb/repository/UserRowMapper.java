package com.example.projectmanageweb.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.model.Role;
import com.example.projectmanageweb.model.User;

@Repository
public class UserRowMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException{
		User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
  
        try {
        	u.setPhoneNumber(rs.getString("phone_number")); 
        } catch (SQLException ignore) {
        		
        }
        try {
        	u.setAvatarUrl(rs.getString("avatar_url")); 
        }catch (SQLException ignore) {}
        u.setRoleId(rs.getInt("role_id"));
        try {
            var cAt = rs.getTimestamp("created_at");
            var uAt = rs.getTimestamp("updated_at");
            if (cAt != null) u.setCreatedAt(cAt.toLocalDateTime());
            if (uAt != null) u.setUpdatedAt(uAt.toLocalDateTime());
        } catch (SQLException ignore) {}
        try {
            Role r = new Role();
            r.setRoleId(rs.getInt("role_id"));
            r.setRoleName(rs.getString("role_name"));
            u.setRole(r);
        } catch (SQLException ignore) {}
        return u;
    }

}
