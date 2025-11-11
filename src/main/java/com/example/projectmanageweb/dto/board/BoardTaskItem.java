package com.example.projectmanageweb.dto.board;

import java.time.LocalDate;

public class BoardTaskItem {
	
	private Integer taskId;
    private String summary;   // map từ tasks.title
    private String type;      // map từ tasks.type (Task/Bug/Feature)
    private String status;    // 'To Do','In Progress','Review','Done'
    private String priority;  // 'Low','Medium','High'
    private LocalDate dueDate;
    private boolean canChangeStatus;

    public boolean isCanChangeStatus() { return canChangeStatus; }
    public void setCanChangeStatus(boolean canChangeStatus) { this.canChangeStatus = canChangeStatus; }

	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
    
    

}
