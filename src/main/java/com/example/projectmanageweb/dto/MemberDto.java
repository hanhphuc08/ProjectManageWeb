package com.example.projectmanageweb.dto;

import java.util.List;

public class MemberDto {
	
	private int projectMemberId;
    private int userId;
    private String fullName;
    private String role;     
    private List<String> skills;
	public int getProjectMemberId() {
		return projectMemberId;
	}
	public void setProjectMemberId(int projectMemberId) {
		this.projectMemberId = projectMemberId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<String> getSkills() {
		return skills;
	}
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
    
    

}
