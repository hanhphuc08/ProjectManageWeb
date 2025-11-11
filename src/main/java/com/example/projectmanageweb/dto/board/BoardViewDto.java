package com.example.projectmanageweb.dto.board;

import java.util.List;

import com.example.projectmanageweb.dto.MemberDto;
import com.example.projectmanageweb.dto.TaskCardDto;

public class BoardViewDto {
	private int projectId;
    private String projectName;
    private MemberDto currentUser;

    private List<MemberDto> members;

    private List<TaskCardDto> todo;
    private List<TaskCardDto> inProgress;
    private List<TaskCardDto> review;
    private List<TaskCardDto> done;
    
    private boolean currentUserIsPm;
    
    
	public boolean isCurrentUserIsPm() {
		return currentUserIsPm;
	}
	public void setCurrentUserIsPm(boolean currentUserIsPm) {
		this.currentUserIsPm = currentUserIsPm;
	}
	
	public List<TaskCardDto> getReview() {
		return review;
	}
	public void setReview(List<TaskCardDto> review) {
		this.review = review;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public MemberDto getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(MemberDto currentUser) {
		this.currentUser = currentUser;
	}
	public List<MemberDto> getMembers() {
		return members;
	}
	public void setMembers(List<MemberDto> members) {
		this.members = members;
	}
	public List<TaskCardDto> getTodo() {
		return todo;
	}
	public void setTodo(List<TaskCardDto> todo) {
		this.todo = todo;
	}
	public List<TaskCardDto> getInProgress() {
		return inProgress;
	}
	public void setInProgress(List<TaskCardDto> inProgress) {
		this.inProgress = inProgress;
	}
	public List<TaskCardDto> getDone() {
		return done;
	}
	public void setDone(List<TaskCardDto> done) {
		this.done = done;
	}

}
