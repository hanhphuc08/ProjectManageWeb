package com.example.projectmanageweb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanageweb.dto.ProjectCreateRequest;
import com.example.projectmanageweb.dto.ProjectListItem;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.ProjectRolesRepository;
import com.example.projectmanageweb.repository.ProjectsRepository;
import com.example.projectmanageweb.service.ProjectService;

@Service
public class ProjectsServiceImpl implements ProjectService {

	private final ProjectsRepository projectsRepo;
	private final ProjectMembersRepository membersRepo;
	private final ProjectRolesRepository rolesRepo;
	

	public ProjectsServiceImpl(ProjectsRepository projectsRepo, ProjectMembersRepository membersRepo,
			ProjectRolesRepository rolesRepo) {
		super();
		this.projectsRepo = projectsRepo;
		this.membersRepo = membersRepo;
		this.rolesRepo = rolesRepo;
	}

	@Override
	@Transactional
	public int create(ProjectCreateRequest dto, int creatorUserId) {
        if (dto.getStartDate()!=null && dto.getEndDate()!=null
                && dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date không được sau End date");
        }

        int projectId = projectsRepo.createProject(
                dto.getProjectName(),
                dto.getTypeId(),
                dto.getDescription(),
                dto.getStartDate(),
                dto.getEndDate(),
                creatorUserId
        );

        int pmRoleId = rolesRepo.ensure(projectId, "PM", "Project Manager");
        rolesRepo.ensure(projectId, "MEMBER", "Project Member");

        // gán creator = PM
        membersRepo.addMember(projectId, creatorUserId, pmRoleId);

        return projectId;
    }


	@Override
	public List<ProjectListItem> listForUser(int userId, String q) {
		 return projectsRepo.listForUser(userId, q, false);
	}
	
	@Override
    public List<ProjectListItem> listForUser(int userId, String q, boolean admin) {
        return projectsRepo.listForUser(userId, q, admin);
    }

	@Override
	public ProjectListItem getProjectDetail(int projectId, int userId, boolean admin) {
		return projectsRepo.findProjectDetail(projectId, userId, admin);
	}
	

}
