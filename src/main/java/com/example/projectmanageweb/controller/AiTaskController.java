package com.example.projectmanageweb.controller;

import java.util.List;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.dto.SuggestedTask;
import com.example.projectmanageweb.service.AiTaskService;

@RestController
@RequestMapping("/api/ai")
public class AiTaskController {
	private final AiTaskService aiTaskService;

    public AiTaskController(AiTaskService aiTaskService) {
        this.aiTaskService = aiTaskService;
    }

    // AI gợi ý task cho cả project (chưa cần WBS)
    @PostMapping("/projects/{projectId}/suggest-tasks")
    public List<SuggestedTask> suggestTasksForProject(
            @PathVariable Integer projectId,
            @RequestParam(required = false) String note
    ) {
        return aiTaskService.suggestTasksForProject(projectId, note);
    }

	
}
