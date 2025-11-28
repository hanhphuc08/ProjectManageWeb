package com.example.projectmanageweb.dto;

import java.util.List;

public class UserProfileView {

    // ===== BASIC USER INFO =====
    private Integer userId;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String phoneNumber;
    private String globalRole;      // Admin / PM / Member

    // ===== EXTRA PROFILE (tạm thời chưa có cột trong DB thì để null cũng được) =====
    private String title;
    private String department;
    private String organization;
    private String location;
    
    private String workMode;          // Online / Offline
    private Integer workHoursPerWeek;
    

    public String getWorkMode() {
		return workMode;
	}
	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}
	public Integer getWorkHoursPerWeek() {
		return workHoursPerWeek;
	}
	public void setWorkHoursPerWeek(Integer workHoursPerWeek) {
		this.workHoursPerWeek = workHoursPerWeek;
	}
	// ===== SKILLS =====
    private List<String> skills;

    // ===== STATS =====
    private int activeProjectsCount;
    private int pmProjectsCount;
    private int openTasksCount;
    private Double performanceAvg;  // chưa có bảng performance -> tạm null

    // ===== RECENT ACTIVITY =====
    private List<RecentActivityDto> recentActivities;
    private boolean hasMoreActivities;

    // ===== ACTIVE PROJECTS =====
    private List<ActiveProjectItem> activeProjects;

    // ===== GOALS (dựa trên GoalsRepository) =====
    private GoalStats goalStats;
    private String lastFeedback;    // tạm null, nếu sau này có bảng đánh giá thì gắn vào

    // ===== PERSONAL TASK STATS (có thể làm sau) =====
    private PersonalTaskStats personalTaskStats;

    // getter/setter bên dưới

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getGlobalRole() { return globalRole; }
    public void setGlobalRole(String globalRole) { this.globalRole = globalRole; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public int getActiveProjectsCount() { return activeProjectsCount; }
    public void setActiveProjectsCount(int activeProjectsCount) { this.activeProjectsCount = activeProjectsCount; }

    public int getPmProjectsCount() { return pmProjectsCount; }
    public void setPmProjectsCount(int pmProjectsCount) { this.pmProjectsCount = pmProjectsCount; }

    public int getOpenTasksCount() { return openTasksCount; }
    public void setOpenTasksCount(int openTasksCount) { this.openTasksCount = openTasksCount; }

    public Double getPerformanceAvg() { return performanceAvg; }
    public void setPerformanceAvg(Double performanceAvg) { this.performanceAvg = performanceAvg; }

    public List<RecentActivityDto> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<RecentActivityDto> recentActivities) { this.recentActivities = recentActivities; }

    public boolean isHasMoreActivities() { return hasMoreActivities; }
    public void setHasMoreActivities(boolean hasMoreActivities) { this.hasMoreActivities = hasMoreActivities; }

    public List<ActiveProjectItem> getActiveProjects() { return activeProjects; }
    public void setActiveProjects(List<ActiveProjectItem> activeProjects) { this.activeProjects = activeProjects; }

    public GoalStats getGoalStats() { return goalStats; }
    public void setGoalStats(GoalStats goalStats) { this.goalStats = goalStats; }

    public String getLastFeedback() { return lastFeedback; }
    public void setLastFeedback(String lastFeedback) { this.lastFeedback = lastFeedback; }

    public PersonalTaskStats getPersonalTaskStats() { return personalTaskStats; }
    public void setPersonalTaskStats(PersonalTaskStats personalTaskStats) { this.personalTaskStats = personalTaskStats; }
}
