package com.example.projectmanageweb.model;

public class ProjectMemberSkill {
	private int skillId;
    private int projectMemberId;
    private String skillName;
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public int getProjectMemberId() {
		return projectMemberId;
	}
	public void setProjectMemberId(int projectMemberId) {
		this.projectMemberId = projectMemberId;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
    
    

}
