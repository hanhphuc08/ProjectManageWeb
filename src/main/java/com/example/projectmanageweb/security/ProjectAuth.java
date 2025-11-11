package com.example.projectmanageweb.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.projectmanageweb.service.UserService;

@Component("projectAuth")
public class ProjectAuth {
	
	private final JdbcTemplate jdbc;
	private final UserService userService;
	public ProjectAuth(JdbcTemplate jdbc, UserService userService) {
		super();
		this.jdbc = jdbc;
		this.userService = userService;
	}
	
	private boolean isAdmin(Authentication auth) {
		return auth != null && auth.getAuthorities().stream()
				.anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
	}
	private Integer currentUserId(Authentication auth) {
		if(auth == null) return null;
		return userService.findByEmail(auth.getName()).map(u -> u.getUserId()).orElse(null);
	}
	
	public boolean isMember(Integer projectId, Authentication auth) {
		Integer uid = currentUserId(auth);
		if(projectId == null || uid == null) return false;
		
		Integer cnt = jdbc.queryForObject(
				"Select Count(*) from project_members where project_id = ? And user_id = ?",
				Integer.class, projectId, uid);
		return cnt != null && cnt > 0;
	}
	
	public boolean isPm(Integer projectId, Authentication auth) {
		Integer uid = currentUserId(auth);
		if(projectId == null && uid == null) return false;
		
		Integer cnt = jdbc.queryForObject(
				"Select count(*) from project_members where project_id = ? and role_in_project = 'PM'",
				Integer.class, projectId, uid);
		
		return cnt != null && cnt > 0;
	}
	
	public boolean canView(Integer projectId, Authentication auth) {
		return isAdmin(auth) || isMember(projectId, auth);
	}
	
	public boolean canEdit(Integer projectId, Authentication auth) {
		return isAdmin(auth) || isPm(projectId, auth);
	}

}
