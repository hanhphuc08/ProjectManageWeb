package com.example.projectmanageweb.dto.board;

import java.util.List;

public class ProjectBoardView {

	private Integer projectId;
    private String projectName;

    private boolean currentUserIsPm;

    private List<BoardTaskItem> todo;
    private List<BoardTaskItem> inProgress;
    private List<BoardTaskItem> review;
    private List<BoardTaskItem> done;

    private List<BoardMemberItem> members;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isCurrentUserIsPm() {
		return currentUserIsPm;
	}

	public void setCurrentUserIsPm(boolean currentUserIsPm) {
		this.currentUserIsPm = currentUserIsPm;
	}

	public List<BoardTaskItem> getTodo() {
		return todo;
	}

	public void setTodo(List<BoardTaskItem> todo) {
		this.todo = todo;
	}

	public List<BoardTaskItem> getInProgress() {
		return inProgress;
	}

	public void setInProgress(List<BoardTaskItem> inProgress) {
		this.inProgress = inProgress;
	}

	public List<BoardTaskItem> getReview() {
		return review;
	}

	public void setReview(List<BoardTaskItem> review) {
		this.review = review;
	}

	public List<BoardTaskItem> getDone() {
		return done;
	}

	public void setDone(List<BoardTaskItem> done) {
		this.done = done;
	}

	public List<BoardMemberItem> getMembers() {
		return members;
	}

	public void setMembers(List<BoardMemberItem> members) {
		this.members = members;
	}
    
    
    
}
