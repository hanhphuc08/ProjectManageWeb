package com.example.projectmanageweb.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.ProjectListItem;
import com.example.projectmanageweb.dto.ProjectMemberDto;
import com.example.projectmanageweb.model.ProjectMember;
import com.example.projectmanageweb.model.ProjectMemberSkill;
import com.example.projectmanageweb.model.ProjectRole;

@Repository
public class ProjectMembersRepository {

	private final JdbcTemplate jdbc;

	public ProjectMembersRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public void addMember(int projectId, int userId, int projectRoleId) {
		jdbc.update("""
				    INSERT INTO project_members(project_id, user_id, project_role_id,
				                                allocation_pct, availability, added_at)
				    VALUES (?,?,?,100,'FULL_TIME', NOW())
				""", projectId, userId, projectRoleId);
	}

	public boolean isPmOfProject(int projectId, int userId) {
		Integer c = jdbc.queryForObject("""
				  SELECT COUNT(*) FROM project_members
				  WHERE project_id=? AND user_id=? AND role_in_project='PM'
				""", Integer.class, projectId, userId);
		return c != null && c > 0;
	}

	public boolean isMember(int projectId, int userId) {
		String sql = """
				SELECT COUNT(*) FROM project_members
				WHERE project_id = ? AND user_id = ?
				""";
		Integer c = jdbc.queryForObject(sql, Integer.class, projectId, userId);
		return c != null && c > 0;
	}

	// lấy list member để show ở màn hình
	public List<ProjectMember> findMembersByProject(int projectId) {
		String sql = """
				    SELECT project_member_id, project_id, user_id, role_in_project
				    FROM project_members
				    WHERE project_id = ?
				""";

		return jdbc.query(sql, (rs, i) -> {
			ProjectMember m = new ProjectMember();
			m.setProjectMemberId(rs.getInt("project_member_id"));
			m.setProjectId(rs.getInt("project_id"));
			m.setUserId(rs.getInt("user_id"));
			m.setRole(rs.getString("role_in_project"));
			return m;
		}, projectId);
	}

	// Lấy skill của một project member
	public List<ProjectMemberSkill> findSkillsByProjectMember(int pmId) {
		String sql = """
				    SELECT skill_id, project_member_id, skill_name
				    FROM project_member_skills
				    WHERE project_member_id = ?
				""";

		return jdbc.query(sql, (rs, i) -> {
			ProjectMemberSkill s = new ProjectMemberSkill();
			s.setSkillId(rs.getInt("skill_id"));
			s.setProjectMemberId(rs.getInt("project_member_id"));
			s.setSkillName(rs.getString("skill_name"));
			return s;
		}, pmId);
	}

	// Lấy member theo userId + projectId
	public ProjectMember findByUserAndProject(int userId, int projectId) {
		String sql = """
				    SELECT project_member_id, project_id, user_id, role_in_project
				    FROM project_members
				    WHERE user_id = ? AND project_id = ?
				""";

		List<ProjectMember> list = jdbc.query(sql, (rs, i) -> {
			ProjectMember m = new ProjectMember();
			m.setProjectMemberId(rs.getInt("project_member_id"));
			m.setProjectId(rs.getInt("project_id"));
			m.setUserId(rs.getInt("user_id"));
			m.setRole(rs.getString("role_in_project"));
			return m;
		}, userId, projectId);

		return list.isEmpty() ? null : list.get(0);
	}

	public boolean existsByProjectAndUser(int projectId, int userId) {
		String sql = """
				SELECT COUNT(*)
				FROM project_members
				WHERE project_id = ? AND user_id = ?
				""";
		Integer cnt = jdbc.queryForObject(sql, Integer.class, projectId, userId);
		return cnt != null && cnt > 0;
	}

	public int insertMember(int projectId, int userId, String roleInProject, Integer projectRoleId,
			Integer allocationPct, String availability) {

		String role = (roleInProject == null) ? "Member" : roleInProject;
		int alloc = (allocationPct == null) ? 100 : allocationPct;
		String avail = (availability == null) ? "FULL_TIME" : availability;

		String sql = """
				INSERT INTO project_members
				  (project_id, user_id, role_in_project, project_role_id, allocation_pct, availability)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		KeyHolder kh = new GeneratedKeyHolder();
		jdbc.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, projectId);
			ps.setInt(2, userId);
			ps.setString(3, role);
			if (projectRoleId != null) {
				ps.setInt(4, projectRoleId);
			} else {
				ps.setNull(4, Types.INTEGER);
			}
			ps.setInt(5, alloc);
			ps.setString(6, avail);
			return ps;
		}, kh);

		return kh.getKey().intValue();
	}

	public void addSkillForMember(int projectMemberId, String skillName) {
		String sql = """
				INSERT INTO project_member_skills (project_member_id, skill_name)
				VALUES (?, ?)
				""";
		jdbc.update(sql, projectMemberId, skillName);
	}

	public List<ProjectRole> findProjectRoles(int projectId) {
		String sql = """
				SELECT project_role_id, role_code, role_name
				FROM project_roles
				WHERE project_id = ?
				ORDER BY role_name
				""";
		return jdbc.query(sql, (rs, rowNum) -> {
			ProjectRole r = new ProjectRole();
			r.setProjectRoleId(rs.getInt("project_role_id"));
			r.setRoleCode(rs.getString("role_code"));
			r.setRoleName(rs.getString("role_name"));
			return r;
		}, projectId);
	}
	
	// tìm theo id
	public ProjectMember findById(int projectMemberId) {
	    String sql = """
	            SELECT project_member_id, project_id, user_id, role_in_project
	            FROM project_members
	            WHERE project_member_id = ?
	            """;

	    List<ProjectMember> list = jdbc.query(sql, (rs, i) -> {
	        ProjectMember m = new ProjectMember();
	        m.setProjectMemberId(rs.getInt("project_member_id"));
	        m.setProjectId(rs.getInt("project_id"));
	        m.setUserId(rs.getInt("user_id"));
	        m.setRole(rs.getString("role_in_project"));
	        return m;
	    }, projectMemberId);

	    return list.isEmpty() ? null : list.get(0);
	}

	// đếm số PM trong dự án
	public int countPmOfProject(int projectId) {
	    String sql = """
	            SELECT COUNT(*)
	            FROM project_members
	            WHERE project_id = ? AND role_in_project = 'PM'
	            """;
	    Integer c = jdbc.queryForObject(sql, Integer.class, projectId);
	    return c == null ? 0 : c;
	}

	// xóa 1 member (cascase sẽ xóa luôn skills)
	public void deleteMember(int projectMemberId) {
	    String sql = "DELETE FROM project_members WHERE project_member_id = ?";
	    jdbc.update(sql, projectMemberId);
	}

}
