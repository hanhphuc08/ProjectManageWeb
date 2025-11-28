package com.example.projectmanageweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskBasic {
	private int taskId;
    private String title;
    private String status;
    private String type;
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
    
    

}
