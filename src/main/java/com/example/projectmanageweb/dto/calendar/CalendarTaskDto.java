package com.example.projectmanageweb.dto.calendar;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CalendarTaskDto {
	private int taskId;
    private String title;
    private String status;        // enum trong tasks: To Do / In Progress / Review / Done :contentReference[oaicite:1]{index=1}
    private String type;          // cột type trong tasks: Task / Bug / Feature :contentReference[oaicite:2]{index=2}
    private String assigneeName;  // join task_assignees -> users.full_name :contentReference[oaicite:3]{index=3}
    private LocalDate dueDate;
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
    
    

}
