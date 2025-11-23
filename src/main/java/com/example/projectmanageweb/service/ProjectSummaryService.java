package com.example.projectmanageweb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.CountDto;
import com.example.projectmanageweb.dto.MetricDto;
import com.example.projectmanageweb.dto.ProjectSummaryDto;
import com.example.projectmanageweb.dto.RecentActivityDto;
import com.example.projectmanageweb.repository.TasksRepository;

@Service
public class ProjectSummaryService {
	private final TasksRepository tasksRepo;

    public ProjectSummaryService(TasksRepository tasksRepo) {
        this.tasksRepo = tasksRepo;
    }

    public ProjectSummaryDto buildSummary(int projectId) {
        MetricDto metrics = tasksRepo.getMetricsByProject(projectId);
        List<CountDto> statusCounts = tasksRepo.countByStatus(projectId);
        List<CountDto> priorityCounts = tasksRepo.countByPriority(projectId);
        List<CountDto> typeCounts = tasksRepo.countByType(projectId);
        List<RecentActivityDto> recent = tasksRepo.findRecentActivities(projectId, 20);

        ProjectSummaryDto dto = new ProjectSummaryDto();
        dto.setMetrics(metrics);
        dto.setStatusCounts(statusCounts);
        dto.setPriorityCounts(priorityCounts);
        dto.setTypeCounts(typeCounts);
        dto.setRecentActivities(recent);
        return dto;
    }

}
