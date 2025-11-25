package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.dto.GoalTaskDto;
import com.example.projectmanageweb.model.Task;
import com.example.projectmanageweb.repository.GoalsRepository;

@RestController
@RequestMapping("/api/projects/{projectId}/goals")
public class GoalApiController {
	
	private final GoalsRepository goalsRepo;

    public GoalApiController(GoalsRepository goalsRepo) {
        this.goalsRepo = goalsRepo;
    }

    @GetMapping("/{goalId}/available-tasks")
    @PreAuthorize("isAuthenticated()")
    public List<Task> availableTasks(@PathVariable int projectId,
                                     @PathVariable int goalId) {
        return goalsRepo.findAvailableTasks(projectId, goalId);
    }
    
    @GetMapping("/{goalId}/tasks")
    @PreAuthorize("isAuthenticated()")
    public List<GoalTaskDto> linkedTasks(@PathVariable int projectId,
                                         @PathVariable int goalId) {
        return goalsRepo.listTasksOfGoal(projectId, goalId);
    }

}
