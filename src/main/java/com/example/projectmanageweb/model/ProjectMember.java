package com.example.projectmanageweb.model;

import java.time.Instant;
import java.time.LocalDateTime;

public class ProjectMember {
	private int projectMemberId;
    private int projectId;
    private int userId;
    private String role;
    private String roleInProject;      // 'PM' / 'Member'
    private Integer projectRoleId;     // FK -> project_roles.project_role_id
    private Integer allocationPct;     // 0..100
    private String availability;       // 'FULL_TIME' / 'PART_TIME'
    private LocalDateTime addedAt;
	public int getProjectMemberId() {
		return projectMemberId;
	}
	public void setProjectMemberId(int projectMemberId) {
		this.projectMemberId = projectMemberId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRoleInProject() {
		return roleInProject;
	}
	public void setRoleInProject(String roleInProject) {
		this.roleInProject = roleInProject;
	}
	public Integer getProjectRoleId() {
		return projectRoleId;
	}
	public void setProjectRoleId(Integer projectRoleId) {
		this.projectRoleId = projectRoleId;
	}
	public Integer getAllocationPct() {
		return allocationPct;
	}
	public void setAllocationPct(Integer allocationPct) {
		this.allocationPct = allocationPct;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public LocalDateTime getAddedAt() {
		return addedAt;
	}
	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}

}
