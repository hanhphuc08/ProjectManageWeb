package com.example.projectmanageweb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanageweb.repository.GoalsRepository;
import com.example.projectmanageweb.repository.TaskAssigneesRepository;
import com.example.projectmanageweb.repository.TasksRepository;

@Service
public class TaskService {

	private final TasksRepository tasksRepo;
    private final TaskAssigneesRepository assigneeRepo;
    private final GoalsRepository goalsRepo;

    
    public TaskService(TasksRepository tasksRepo, TaskAssigneesRepository assigneeRepo, GoalsRepository goalsRepo) {
		super();
		this.tasksRepo = tasksRepo;
		this.assigneeRepo = assigneeRepo;
		this.goalsRepo = goalsRepo;
	}


	public void deleteTask(int projectId, int taskId) {
        assigneeRepo.deleteByTask(taskId);

        tasksRepo.deleteById(taskId);
    }
	@Transactional
    public void updateTaskStatus(int taskId, String newStatus) {
        tasksRepo.updateStatus(taskId, newStatus);

        List<Integer> goalIds = goalsRepo.findGoalIdsByTask(taskId);
        for (Integer gid : goalIds) {
            goalsRepo.recomputeGoalStatus(gid);
        }
    }
}
