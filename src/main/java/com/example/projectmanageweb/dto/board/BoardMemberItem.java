package com.example.projectmanageweb.dto.board;

public class BoardMemberItem {
	
	private Integer projectMemberId;
    private Integer userId;
    private String fullName;
    private String roleInProject; // PM / Member
    private String avatarUrl;
	public Integer getProjectMemberId() {
		return projectMemberId;
	}
	public void setProjectMemberId(Integer projectMemberId) {
		this.projectMemberId = projectMemberId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getRoleInProject() {
		return roleInProject;
	}
	public void setRoleInProject(String roleInProject) {
		this.roleInProject = roleInProject;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
    
    

}
