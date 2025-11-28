package com.example.projectmanageweb.dto;

import java.util.List;

public class TaskResourceGroupDto {
	private int taskId;
    private String taskTitle;
    private String status;
    private String type;

    private List<ResourceItemDto> files;


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
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

    public List<ResourceItemDto> getFiles() {
        return files;
    }

    public void setFiles(List<ResourceItemDto> files) {
        this.files = files;
    }

}
