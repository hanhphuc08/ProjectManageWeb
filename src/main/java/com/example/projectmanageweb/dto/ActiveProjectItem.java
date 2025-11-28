package com.example.projectmanageweb.dto;

public class ActiveProjectItem {

    private int projectId;
    private String projectName;
    private String roleInProject;   // PM / Member
    private String status;          // Planned / In Progress / Done...
    private String dateRange;       // "dd/MM/yyyy - dd/MM/yyyy"
    private int openTasks;          // task của user này trong project, chưa Done
    private int doneTasks;          // task của user này trong project, đã Done

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getRoleInProject() { return roleInProject; }
    public void setRoleInProject(String roleInProject) { this.roleInProject = roleInProject; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }

    public int getOpenTasks() { return openTasks; }
    public void setOpenTasks(int openTasks) { this.openTasks = openTasks; }

    public int getDoneTasks() { return doneTasks; }
    public void setDoneTasks(int doneTasks) { this.doneTasks = doneTasks; }
}
