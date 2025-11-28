package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.dto.MemberTaskItem;
import com.example.projectmanageweb.service.MemberTaskService;
import com.example.projectmanageweb.service.ProjectMembersService;
import com.example.projectmanageweb.service.UserService;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
public class MemberTaskApiController {
	
	private final MemberTaskService memberTaskService;
    private final UserService userService;
    private final ProjectMembersService membersService;

    public MemberTaskApiController(MemberTaskService memberTaskService,
                                   UserService userService,
                                   ProjectMembersService membersService) {
        this.memberTaskService = memberTaskService;
        this.userService = userService;
        this.membersService = membersService;
    }

    @GetMapping("/{userId}/tasks")
    @PreAuthorize("isAuthenticated()")
    public List<MemberTaskItem> listTasks(
            @PathVariable int projectId,
            @PathVariable int userId,
            Authentication auth
    ) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();

        // Optional: kiểm tra người đang xem cũng là member của project
        // membersService.requireMember(projectId, me.getUserId());

        return memberTaskService.listTasksForMember(projectId, userId);
    }

}
