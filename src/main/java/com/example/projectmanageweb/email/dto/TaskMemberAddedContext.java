package com.example.projectmanageweb.email.dto;

public class TaskMemberAddedContext {
	
	private final String toEmail;
    private final String memberName;
    private final String projectName;
    private final String taskTitle;
    private final String assignedByName;
	public TaskMemberAddedContext(String toEmail, String memberName, String projectName, String taskTitle,
			String assignedByName) {
		super();
		this.toEmail = toEmail;
		this.memberName = memberName;
		this.projectName = projectName;
		this.taskTitle = taskTitle;
		this.assignedByName = assignedByName;
	}
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
	public String getAssignedByName() {
		return assignedByName;
	}
    
    

}
