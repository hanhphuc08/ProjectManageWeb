package com.example.projectmanageweb.dto;

import java.time.LocalDateTime;

public class ResourceItemDto {

    private int resourceId;
    private Integer taskId;
    private String taskTitle;
    private String taskStatus;
    private String taskType;          // NEW: loại task (Task/Bug/Story...)

    private String originalName;
    private String contentType;
    private long sizeBytes;

    private Integer uploaderId;       // NEW
    private String uploaderName;
    private String uploaderRoleInProject; // NEW

    private LocalDateTime uploadedAt;

    // ===== GET/SET =====
    public int getResourceId() { return resourceId; }
    public void setResourceId(int resourceId) { this.resourceId = resourceId; }

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public String getTaskTitle() { return taskTitle; }
    public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }

    public String getTaskStatus() { return taskStatus; }
    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

    public Integer getUploaderId() { return uploaderId; }
    public void setUploaderId(Integer uploaderId) { this.uploaderId = uploaderId; }

    public String getUploaderName() { return uploaderName; }
    public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }

    public String getUploaderRoleInProject() { return uploaderRoleInProject; }
    public void setUploaderRoleInProject(String uploaderRoleInProject) {
        this.uploaderRoleInProject = uploaderRoleInProject;
    }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    // ===== Helper cho FE (sizeHuman) =====
    public String getSizeHuman() {
        long bytes = sizeBytes;
        if (bytes < 1024) return bytes + " B";
        double kb = bytes / 1024.0;
        if (kb < 1024) return String.format("%.1f KB", kb);
        double mb = kb / 1024.0;
        if (mb < 1024) return String.format("%.1f MB", mb);
        double gb = mb / 1024.0;
        return String.format("%.1f GB", gb);
    }
}
