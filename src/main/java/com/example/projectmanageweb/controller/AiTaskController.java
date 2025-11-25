package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.dto.AiSuggestResult;
import com.example.projectmanageweb.dto.SuggestedTask;
import com.example.projectmanageweb.service.AiTaskService;
import com.example.projectmanageweb.service.ProjectMembersService;
import com.example.projectmanageweb.service.UserService;

@RestController
@RequestMapping("/api/ai")
public class AiTaskController {
	private final AiTaskService aiTaskService;
    private final UserService userService;
    private final ProjectMembersService membersService;

    public AiTaskController(
            AiTaskService aiTaskService,
            UserService userService,
            ProjectMembersService membersService
    ) {
        this.aiTaskService = aiTaskService;
        this.userService = userService;
        this.membersService = membersService;
    }
    
 // ========== 1) Gợi ý task mới + ETA ==========
    @PostMapping("/projects/{projectId}/suggest-new-tasks")
    @PreAuthorize("isAuthenticated()")
    public AiSuggestResult suggestNewTasks(
            @PathVariable Integer projectId,
            @RequestParam(required = false) String note,
            Authentication auth
    ) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();
        membersService.requirePm(projectId, me.getUserId());

        return aiTaskService.suggestNewTasksAndEta(projectId, note);
    }

    // AI gợi ý task cho cả project (chưa cần WBS)
    @PostMapping("/projects/{projectId}/suggest-tasks")
    public List<SuggestedTask> suggestTasksForProject(
            @PathVariable Integer projectId,
            @RequestParam(required = false) String note
    ) {
        return aiTaskService.suggestTasksForProject(projectId, note);
    }
    
    
    @PostMapping("/projects/{projectId}/apply-ai-tasks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> applySuggestedTasks(
            @PathVariable Integer projectId,
            @RequestBody List<SuggestedTask> tasks,
            Authentication auth
    ) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();
        membersService.requirePm(projectId, me.getUserId());

        aiTaskService.saveSuggestedTasks(projectId, tasks, me.getUserId());

        return ResponseEntity.ok().build();
    }

	
}
