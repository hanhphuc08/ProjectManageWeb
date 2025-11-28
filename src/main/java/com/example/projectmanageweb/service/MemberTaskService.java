package com.example.projectmanageweb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanageweb.dto.MemberTaskItem;
import com.example.projectmanageweb.repository.TasksRepository;

@Service
public class MemberTaskService {
	private final TasksRepository tasksRepository;

    public MemberTaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberTaskItem> listTasksForMember(int projectId, int userId) {
        return tasksRepository.findByProjectAndAssignee(projectId, userId);
    }

}
