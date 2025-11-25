package com.example.projectmanageweb.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.dto.GoalCreateRequest;
import com.example.projectmanageweb.service.GoalService;
import com.example.projectmanageweb.service.ProjectMembersService;
import com.example.projectmanageweb.service.UserService;

@Controller
public class GoalController {
	
	private final GoalService goalService;
    private final UserService userService;
    private final ProjectMembersService membersService; 

    public GoalController(GoalService goalService,
                          UserService userService,
                          ProjectMembersService membersService) {
        this.goalService = goalService;
        this.userService = userService;
        this.membersService = membersService;
    }

    @PostMapping("/projects/{projectId}/goals")
    @PreAuthorize("isAuthenticated()")
    public String createGoal(@PathVariable int projectId,
                             GoalCreateRequest form,
                             Authentication auth,
                             RedirectAttributes ra) {

        var me = userService.findByEmail(auth.getName()).orElseThrow();

        membersService.requirePm(projectId, me.getUserId());

        LocalDate target = form.getTargetDate();

        goalService.createGoal(
                projectId,
                form.getTitle(),
                form.getDescription(),
                target,
                form.getOwnerId() != null ? form.getOwnerId() : me.getUserId()
        );

        ra.addFlashAttribute("goalCreated", true);

        return "redirect:/projects/" + projectId + "#goals";
    }
    
    @PostMapping("projects/{projectId}/goals/{goalId}/tasks")
    @PreAuthorize("isAuthenticated()")
    public String addTasksToGoal(@PathVariable int projectId,
                                 @PathVariable int goalId,
                                 @RequestParam(name = "taskIds", required = false)
                                 List<Integer> taskIds,
                                 RedirectAttributes ra) {

        if (taskIds != null && !taskIds.isEmpty()) {
            goalService.addTasksToGoal(goalId, taskIds);
            ra.addFlashAttribute("goalUpdated", true);
        }

        return "redirect:/projects/" + projectId + "#goals";
    }
    
    	// Xoá 1 task khỏi goal
    @PostMapping("/projects/{projectId}/goals/{goalId}/tasks/{taskId}/unlink")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> unlinkTaskFromGoal(
            @PathVariable int projectId,
            @PathVariable int goalId,
            @PathVariable int taskId
    ) {
        // ở đây có thể check projectId có khớp goal/project không, nhưng tạm skip
        goalService.removeTaskFromGoal(goalId, taskId);
        return ResponseEntity.ok().build();
    }

    // (tuỳ chọn) xoá nhiều task khỏi goal 1 lần
    @PostMapping("/{projectId}/goals/{goalId}/tasks/unlink-batch")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> unlinkTasksFromGoal(
            @PathVariable int projectId,
            @PathVariable int goalId,
            @RequestBody List<Integer> taskIds
    ) {
        goalService.removeTasksFromGoal(goalId, taskIds);
        return ResponseEntity.ok().build();
    }

}
