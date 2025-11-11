package com.example.projectmanageweb.dto;

import java.util.List;

public class ProjectMemberAddForm {
	private Integer userId;
    private Integer projectRoleId;       // chọn DEV/TEST/BA...
    private Integer allocationPct;       // optional
    private String availability;         // FULL_TIME / PART_TIME
    private String skills;
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
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
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	
    
    

}
