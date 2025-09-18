package com.example.projectmanageweb.model;

import java.time.Instant;

public class User {
	
	private int userId;
	private String fullName;
	private String email;
	private String passwordHash;
	private String avatarUrl;
	private String phoneNumber;
	private String workMode;
	private int workHoursPerWeek;
	private Role role;
	private Instant createdAt;
	private Instant updatedAt;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getWorkMode() {
		return workMode;
	}
	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}
	public int getWorkHoursPerWeek() {
		return workHoursPerWeek;
	}
	public void setWorkHoursPerWeek(int workHoursPerWeek) {
		this.workHoursPerWeek = workHoursPerWeek;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	

}
