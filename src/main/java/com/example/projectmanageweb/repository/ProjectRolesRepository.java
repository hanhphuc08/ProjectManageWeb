package com.example.projectmanageweb.repository;

import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRolesRepository {
	private final JdbcTemplate jdbc;

	public ProjectRolesRepository(JdbcTemplate jdbc) {
		super();
		this.jdbc = jdbc;
	}

	public Integer findId(int projectId, String roleCode) {
        // C1: dùng try/catch
        try {
            return jdbc.queryForObject("""
                SELECT project_role_id
                FROM project_roles
                WHERE project_id = ? AND role_code = ?
            """, Integer.class, projectId, roleCode);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null; // 0 rows -> ok
        }
    }

    public int create(int projectId, String roleCode, String roleName) {
        var kh = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbc.update(conn -> {
            var ps = conn.prepareStatement("""
                INSERT INTO project_roles(project_id, role_code, role_name)
                VALUES (?,?,?)
            """, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, projectId);
            ps.setString(2, roleCode);
            ps.setString(3, roleName);
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    /** Idempotent */
    public int ensure(int projectId, String roleCode, String roleName) {
        Integer id = findId(projectId, roleCode);
        if (id != null) return id;
        return create(projectId, roleCode, roleName);
    }
}
