package com.example.projectmanageweb.email.dto;

import java.time.LocalDate;

public class DeadlineReminderContext {
	private final String toEmail;
    private final String memberName;
    private final String projectName;
    private final String taskTitle;
    private final LocalDate dueDate;
	public String getToEmail() {
		return toEmail;
	}
	public String getMemberName() {
		return memberName;
	}
	public String getProjectName() {
		return projectName;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public DeadlineReminderContext(String toEmail, String memberName, String projectName, String taskTitle,
			LocalDate dueDate) {
		super();
		this.toEmail = toEmail;
		this.memberName = memberName;
		this.projectName = projectName;
		this.taskTitle = taskTitle;
		this.dueDate = dueDate;
	}
    
    

}
