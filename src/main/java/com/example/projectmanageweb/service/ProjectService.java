package com.example.projectmanageweb.service;

import java.util.List;

import com.example.projectmanageweb.dto.ProjectCreateRequest;
import com.example.projectmanageweb.dto.ProjectListItem;

public interface ProjectService {

	int create(ProjectCreateRequest  dto, int creatorUserId);
	default List<ProjectListItem> listForUser(int userId, String q) {
        return listForUser(userId, q, false);
    }
	 List<ProjectListItem> listForUser(int userId, String q, boolean admin);
	 
	 ProjectListItem getProjectDetail(int projectId, int userId, boolean admin);

}
