package com.example.projectmanageweb.model;

import java.time.Instant;

public class User {
	
	 private Integer userId;
	    private String fullName;
	    private String email;
	    private String passwordHash;
	    private String phoneNumber;     
	    private String avatarUrl;
	    private Integer roleId;
	    private java.time.LocalDateTime createdAt;
	    private java.time.LocalDateTime updatedAt;
	    private Role role;
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
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getAvatarUrl() {
			return avatarUrl;
		}
		public void setAvatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
		}
		public Integer getRoleId() {
			return roleId;
		}
		public void setRoleId(Integer roleId) {
			this.roleId = roleId;
		}
		public java.time.LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(java.time.LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public java.time.LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}
		public Role getRole() {
			return role;
		}
		public void setRole(Role role) {
			this.role = role;
		}
	    
	    
}
