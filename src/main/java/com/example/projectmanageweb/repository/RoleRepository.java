package com.example.projectmanageweb.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.model.Role;

@Repository
public class RoleRepository {

	@Autowired JdbcTemplate jdbc;

    public Optional<Role> findByName(String roleName) {
        var list = jdbc.query("SELECT * FROM roles WHERE role_name = ?", (rs,i)->{
            Role r = new Role();
            r.setRoleId(rs.getInt("role_id"));
            r.setRoleName(rs.getString("role_name"));
            return r;
        }, roleName);
        return list.stream().findFirst();
    }
}
