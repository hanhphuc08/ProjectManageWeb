package com.example.projectmanageweb.dto;

import java.util.List;

public class ProjectResourcesView {

    private List<ResourceItemDto> allFiles;
    private int totalFiles;
    private long totalSizeBytes;

    private List<TaskResourceGroupDto> byTask;   // NEW kiểu list group
    private List<UserResourceGroupDto> byUser;   // NEW

    // getters / setters
    public List<ResourceItemDto> getAllFiles() { return allFiles; }
    public void setAllFiles(List<ResourceItemDto> allFiles) { this.allFiles = allFiles; }

    public int getTotalFiles() { return totalFiles; }
    public void setTotalFiles(int totalFiles) { this.totalFiles = totalFiles; }

    public long getTotalSizeBytes() { return totalSizeBytes; }
    public void setTotalSizeBytes(long totalSizeBytes) { this.totalSizeBytes = totalSizeBytes; }

    public List<TaskResourceGroupDto> getByTask() { return byTask; }
    public void setByTask(List<TaskResourceGroupDto> byTask) { this.byTask = byTask; }

    public List<UserResourceGroupDto> getByUser() { return byUser; }
    public void setByUser(List<UserResourceGroupDto> byUser) { this.byUser = byUser; }

    public String getTotalSizeHuman() {
        long bytes = totalSizeBytes;
        if (bytes < 1024) return bytes + " B";
        double kb = bytes / 1024.0;
        if (kb < 1024) return String.format("%.1f KB", kb);
        double mb = kb / 1024.0;
        if (mb < 1024) return String.format("%.1f MB", mb);
        double gb = mb / 1024.0;
        return String.format("%.1f GB", gb);
    }
}

