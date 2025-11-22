package com.example.projectmanageweb.dto;

import java.util.List;

public class SuggestedAssignment {
	private Integer taskId;
    private List<Integer> assigneeIds;   // gợi ý assign ai
    private String reason;              // giải thích ngắn
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public List<Integer> getAssigneeIds() {
		return assigneeIds;
	}
	public void setAssigneeIds(List<Integer> assigneeIds) {
		this.assigneeIds = assigneeIds;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

    

}
