package com.example.projectmanageweb.dto;

import java.util.List;

public class TaskCreateForm {

	private String title;
    private String description;
    private String priority;      
    private String dueDate;
    private List<Integer> assigneeIds;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public List<Integer> getAssigneeIds() {
		return assigneeIds;
	}
	public void setAssigneeIds(List<Integer> assigneeIds) {
		this.assigneeIds = assigneeIds;
	}
    
    
}
