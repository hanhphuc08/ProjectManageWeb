package com.example.projectmanageweb.dto;

import java.time.LocalDate;
import java.util.List;

public class TaskCardDto {
	private int taskId;
    private String key;           
    private String summary;
    private String status;
    private String priority;
    private String type;
    private String dueDate;
    private List<String> assignees;
    private boolean canChangeStatus;
    private List<Integer> assigneeIds;
    
    
    
	public List<Integer> getAssigneeIds() {
		return assigneeIds;
	}
	public void setAssigneeIds(List<Integer> assigneeIds) {
		this.assigneeIds = assigneeIds;
	}
	public boolean isCanChangeStatus() {
		return canChangeStatus;
	}
	public void setCanChangeStatus(boolean canChangeStatus) {
		this.canChangeStatus = canChangeStatus;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public List<String> getAssignees() {
		return assignees;
	}
	public void setAssignees(List<String> assignees) {
		this.assignees = assignees;
	}
    
    

}
