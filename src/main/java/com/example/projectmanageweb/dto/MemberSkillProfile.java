package com.example.projectmanageweb.dto;

import java.util.List;

public class MemberSkillProfile {

	private Integer userId;
    private String fullName;
    private List<String> skills;
    private Integer allocationPct;
    private String availability;
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
	public List<String> getSkills() {
		return skills;
	}
	public void setSkills(List<String> skills) {
		this.skills = skills;
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
    
    
}
