package com.example.projectmanageweb.dto.board;

import java.time.LocalDate;
import java.util.List;

public class BoardTaskItem {

    private Integer taskId;
    private Integer projectId;
    public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	private String summary;
    private String type;
    private String status;
    private String priority;

    private LocalDate dueDate;
    private LocalDate startDate;   // NEW
    private LocalDate createdAt;   // NEW
    private LocalDate endDate;     // NEW (nếu DB có)
    
    private List<String> assignees;    // NEW: tên
    private List<Integer> assigneeIds; // NEW: id
    
    private boolean canChangeStatus;

    // ===== SAFE DATES FOR TIMELINE =====
    public LocalDate getSafeStartDate() {
        if (startDate != null) return startDate;
        if (createdAt != null) return createdAt;
        return LocalDate.now();
    }

    public LocalDate getSafeEndDate() {
        if (dueDate != null) return dueDate;
        if (endDate != null) return endDate;
        return getSafeStartDate().plusDays(1);
    }

    // ===== getters/setters =====
    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<String> getAssignees() { return assignees; }
    public void setAssignees(List<String> assignees) { this.assignees = assignees; }

    public List<Integer> getAssigneeIds() { return assigneeIds; }
    public void setAssigneeIds(List<Integer> assigneeIds) { this.assigneeIds = assigneeIds; }

    public boolean isCanChangeStatus() { return canChangeStatus; }
    public void setCanChangeStatus(boolean canChangeStatus) {
        this.canChangeStatus = canChangeStatus;
    }
}
