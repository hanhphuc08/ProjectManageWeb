package com.example.projectmanageweb.service.impl;

import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.OwnerDashboardView;
import com.example.projectmanageweb.repository.ProjectsRepository;
import com.example.projectmanageweb.repository.TasksRepository;
import com.example.projectmanageweb.repository.UserRepository;
import com.example.projectmanageweb.service.OwnerDashboardService;


@Service
public class OwnerDashboardServiceImpl implements OwnerDashboardService {

	private final UserRepository userRepository;
    private final ProjectsRepository projectsRepository;
    private final TasksRepository tasksRepository;

    public OwnerDashboardServiceImpl(UserRepository userRepository,
                                     ProjectsRepository projectsRepository,
                                     TasksRepository tasksRepository) {
        this.userRepository = userRepository;
        this.projectsRepository = projectsRepository;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public OwnerDashboardView buildDashboard() {
        OwnerDashboardView v = new OwnerDashboardView();

        // ===== USER =====
        v.setTotalUsers(userRepository.countAllUsers());
        v.setNewUsersLast7Days(userRepository.countUsersCreatedWithinDays(7));
        v.setUsersByRole(userRepository.countUsersByRole());

        // ===== PROJECT =====
        v.setTotalProjects(projectsRepository.countAllProjects());
        v.setProjectByStatus(projectsRepository.countProjectsByStatus());
        v.setRecentProjects(projectsRepository.findRecentProjects(5)); // 5 project gần nhất

        // ===== TASK =====
        v.setTotalTasks(tasksRepository.countAllTasks());
        v.setTaskByStatus(tasksRepository.countTasksByStatus());

        return v;
    }
}
