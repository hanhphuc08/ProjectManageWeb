package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.dto.SuggestedAssignment;
import com.example.projectmanageweb.service.BoardService;
import com.example.projectmanageweb.service.UserService;

@RestController
@RequestMapping("/projects")
public class AiAssignApplyController {
	
	private final BoardService boardService;
    private final UserService userService;

    public AiAssignApplyController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @PostMapping("/{projectId}/tasks/apply-ai-assignments")
    public ResponseEntity<?> applyAiAssignments(
            @PathVariable int projectId,
            @RequestBody List<SuggestedAssignment> assigns,
            Authentication auth
    ) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();
        boardService.applyAssignments(projectId, me.getUserId(), assigns);
        return ResponseEntity.ok().build();
    }

}
