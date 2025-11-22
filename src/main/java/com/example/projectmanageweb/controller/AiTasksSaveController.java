package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.dto.SuggestedTask;
import com.example.projectmanageweb.service.AiTaskService;
import com.example.projectmanageweb.service.UserService;

@RestController
@RequestMapping("/projects")
public class AiTasksSaveController {
	private final AiTaskService aiTaskService;
	private final UserService userService;

    
    public AiTasksSaveController(AiTaskService aiTaskService, UserService userService) {
		super();
		this.aiTaskService = aiTaskService;
		this.userService = userService;
	}

	@PostMapping("/{projectId}/tasks/from-ai")
    public ResponseEntity<?> saveTasksFromAi(
            @PathVariable Integer projectId,
            @RequestBody List<SuggestedTask> suggestions,
            Authentication auth
    ) {

    	var email = auth.getName();
        var me = userService.findByEmail(email).orElseThrow();
        int currentUserId = me.getUserId();

        aiTaskService.saveSuggestedTasks(projectId, suggestions, currentUserId);

        return ResponseEntity.ok().build();
    }
	

}
