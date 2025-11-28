package com.example.projectmanageweb.dto;

import java.util.List;

public class UserResourceGroupDto {
	private Integer userId;
    private String fullName;
    private String roleInProject;  // Member / PM / QA...

    private List<ResourceItemDto> files;

    // getters / setters

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleInProject() {
        return roleInProject;
    }

    public void setRoleInProject(String roleInProject) {
        this.roleInProject = roleInProject;
    }

    public List<ResourceItemDto> getFiles() {
        return files;
    }

    public void setFiles(List<ResourceItemDto> files) {
        this.files = files;
    }

}
