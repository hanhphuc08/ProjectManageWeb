package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.service.BoardService;
import com.example.projectmanageweb.service.UserService;

@Controller
public class ProjectTaskController {
	private final BoardService boardService;
    private final UserService userService;

    public ProjectTaskController(BoardService boardService,
                                 UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }
    
    @PostMapping("/projects/{projectId}/tasks/{taskId}/status")
    @PreAuthorize("isAuthenticated()")
    public String changeStatus(@PathVariable int projectId,
                               @PathVariable int taskId,
                               @RequestParam("status") String statusCode,
                               @RequestParam(name = "assigneeIds", required = false)
                               List<Integer> assigneeIds,
                               Authentication auth,
                               RedirectAttributes ra) {

        var email = auth.getName();
        var me = userService.findByEmail(email).orElseThrow();
        int currentUserId = me.getUserId();

        try {
            boardService.updateTaskStatusAndAssignees(projectId, currentUserId, taskId, statusCode, assigneeIds);
            ra.addFlashAttribute("successMessage", "Cập nhật trạng thái task thành công!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/projects/" + projectId + "#board-content";
    }

}
