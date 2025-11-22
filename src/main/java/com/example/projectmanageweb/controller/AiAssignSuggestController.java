package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.dto.SuggestedAssignment;
import com.example.projectmanageweb.service.AiTaskService;
import com.example.projectmanageweb.service.ProjectMembersService;
import com.example.projectmanageweb.service.UserService;

@RestController
@RequestMapping("/api/ai")
public class AiAssignSuggestController {
	
	private final AiTaskService aiTaskService;
    private final UserService userService;
    private final ProjectMembersService membersService; 

    public AiAssignSuggestController(AiTaskService aiTaskService,
                                     UserService userService,
                                     ProjectMembersService membersService) {
        this.aiTaskService = aiTaskService;
        this.userService = userService;
        this.membersService = membersService;
    }

    @PostMapping("/projects/{projectId}/suggest-assignments")
    @PreAuthorize("isAuthenticated()")
    public List<SuggestedAssignment> suggestAssignments(
            @PathVariable Integer projectId,
            @RequestParam(required = false) String note,
            Authentication auth
    ) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();
        // optional: check PM ở đây cũng được
        membersService.requirePm(projectId, me.getUserId());

        return aiTaskService.suggestAssignmentsForProject(projectId, note);
    }

}
