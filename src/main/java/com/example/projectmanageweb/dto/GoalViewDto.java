package com.example.projectmanageweb.dto;

import java.time.LocalDate;

public class GoalViewDto {
	
	private Integer goalId;
    private String title;
    private String description;
    private String status;
    private LocalDate targetDate;
    private String ownerName;

    private int totalTasks;
    private int doneTasks;
    private int progressPercent;
	public Integer getGoalId() {
		return goalId;
	}
	public void setGoalId(Integer goalId) {
		this.goalId = goalId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public int getTotalTasks() {
		return totalTasks;
	}
	public void setTotalTasks(int totalTasks) {
		this.totalTasks = totalTasks;
	}
	public int getDoneTasks() {
		return doneTasks;
	}
	public void setDoneTasks(int doneTasks) {
		this.doneTasks = doneTasks;
	}
	public int getProgressPercent() {
		return progressPercent;
	}
	public void setProgressPercent(int progressPercent) {
		this.progressPercent = progressPercent;
	}
    

}
