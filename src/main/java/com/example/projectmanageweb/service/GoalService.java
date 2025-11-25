package com.example.projectmanageweb.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanageweb.dto.GoalViewDto;
import com.example.projectmanageweb.repository.GoalsRepository;

@Service
public class GoalService {

	private final GoalsRepository goalsRepo;

    public GoalService(GoalsRepository goalsRepo) {
        this.goalsRepo = goalsRepo;
    }

    @Transactional(readOnly = true)
    public List<GoalViewDto> listGoals(int projectId) {
        return goalsRepo.listByProject(projectId);
    }

    @Transactional
    public int createGoal(int projectId, String title, String desc,
                          LocalDate targetDate, Integer ownerId) {
        return goalsRepo.createGoal(projectId, title, desc, targetDate, ownerId);
    }

    @Transactional
    public void addTasksToGoal(int goalId, List<Integer> taskIds) {
        goalsRepo.addTasksToGoal(goalId, taskIds);
        goalsRepo.recomputeGoalStatus(goalId);
    }
    
    @Transactional
    public void removeTaskFromGoal(int goalId, int taskId) {
        goalsRepo.removeTaskFromGoal(goalId, taskId);
        goalsRepo.recomputeGoalStatus(goalId); 
    }

    @Transactional
    public void removeTasksFromGoal(int goalId, List<Integer> taskIds) {
        goalsRepo.removeTasksFromGoal(goalId, taskIds);
        goalsRepo.recomputeGoalStatus(goalId);
    }
    
}
