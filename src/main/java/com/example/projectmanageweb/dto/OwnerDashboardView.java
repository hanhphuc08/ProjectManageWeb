package com.example.projectmanageweb.dto;

import java.util.List;

public class OwnerDashboardView {

	// ===== USER =====
    private int totalUsers;
    private int newUsersLast7Days;
    private List<RoleCountDto> usersByRole;

    // ===== PROJECT =====
    private int totalProjects;
    private List<LabelCountDto> projectByStatus;
    private List<RecentProjectDto> recentProjects;

    // ===== TASK =====
    private int totalTasks;
    private List<LabelCountDto> taskByStatus;
	public int getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}
	public int getNewUsersLast7Days() {
		return newUsersLast7Days;
	}
	public void setNewUsersLast7Days(int newUsersLast7Days) {
		this.newUsersLast7Days = newUsersLast7Days;
	}
	public List<RoleCountDto> getUsersByRole() {
		return usersByRole;
	}
	public void setUsersByRole(List<RoleCountDto> usersByRole) {
		this.usersByRole = usersByRole;
	}
	public int getTotalProjects() {
		return totalProjects;
	}
	public void setTotalProjects(int totalProjects) {
		this.totalProjects = totalProjects;
	}
	public List<LabelCountDto> getProjectByStatus() {
		return projectByStatus;
	}
	public void setProjectByStatus(List<LabelCountDto> projectByStatus) {
		this.projectByStatus = projectByStatus;
	}
	public List<RecentProjectDto> getRecentProjects() {
		return recentProjects;
	}
	public void setRecentProjects(List<RecentProjectDto> recentProjects) {
		this.recentProjects = recentProjects;
	}
	public int getTotalTasks() {
		return totalTasks;
	}
	public void setTotalTasks(int totalTasks) {
		this.totalTasks = totalTasks;
	}
	public List<LabelCountDto> getTaskByStatus() {
		return taskByStatus;
	}
	public void setTaskByStatus(List<LabelCountDto> taskByStatus) {
		this.taskByStatus = taskByStatus;
	}
    
    
    
}
