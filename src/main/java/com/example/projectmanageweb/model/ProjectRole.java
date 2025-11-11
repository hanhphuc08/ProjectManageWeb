package com.example.projectmanageweb.model;

public class ProjectRole {
	
	private int projectRoleId;
    private int projectId;
    private String roleCode;  // PM / MEMBER / FE / BE / QA
    private String roleName;
	public int getProjectRoleId() {
		return projectRoleId;
	}
	public void setProjectRoleId(int projectRoleId) {
		this.projectRoleId = projectRoleId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
    
    

}
