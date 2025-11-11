package com.example.projectmanageweb.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AiSuggestion {
	
	private int suggestionId;
    private int projectId;
    private Integer taskId;
    private String type;    // WBS_BREAKDOWN / ASSIGNMENT / DURATION
    private String payloadJson;
    private BigDecimal score;
    private boolean createdByAi;
    private LocalDateTime createdAt;
	public int getSuggestionId() {
		return suggestionId;
	}
	public void setSuggestionId(int suggestionId) {
		this.suggestionId = suggestionId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayloadJson() {
		return payloadJson;
	}
	public void setPayloadJson(String payloadJson) {
		this.payloadJson = payloadJson;
	}
	public BigDecimal getScore() {
		return score;
	}
	public void setScore(BigDecimal score) {
		this.score = score;
	}
	public boolean isCreatedByAi() {
		return createdByAi;
	}
	public void setCreatedByAi(boolean createdByAi) {
		this.createdByAi = createdByAi;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    

}
