package com.example.projectmanageweb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.dto.TaskUpdateForm;
import com.example.projectmanageweb.service.BoardService;
import com.example.projectmanageweb.service.ResourceService;
import com.example.projectmanageweb.service.TaskFileService;
import com.example.projectmanageweb.service.TaskService;
import com.example.projectmanageweb.service.UserService;

@Controller
public class ProjectTaskController {
	private final BoardService boardService;
    private final UserService userService;
    private final TaskService taskService;
    private final ResourceService resourceService;

	public ProjectTaskController(BoardService boardService, UserService userService, TaskService taskService,
			ResourceService resourceService) {
		super();
		this.boardService = boardService;
		this.userService = userService;
		this.taskService = taskService;
		this.resourceService = resourceService;
	}

	@PostMapping("/projects/{projectId}/tasks/{taskId}/status")
    @PreAuthorize("isAuthenticated()")
    public String changeStatus(
            @PathVariable int projectId,
            @PathVariable int taskId,
            @RequestParam("status") String statusCode,
            @RequestParam(name = "assigneeIds", required = false)
            List<Integer> assigneeIds,
            Authentication auth,
            RedirectAttributes ra) {

        var me = userService.findByEmail(auth.getName()).orElseThrow();

        try {
            boardService.updateTaskStatusAndAssignees(
                    projectId, me.getUserId(), taskId, statusCode, assigneeIds
            );
            ra.addFlashAttribute("successMessage", "Cập nhật trạng thái task thành công!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/projects/" + projectId + "#board-content";
    }

    @PostMapping("/projects/{projectId}/tasks/{taskId}/update")
    @PreAuthorize("isAuthenticated()")
    public String updateTaskFull(
            @PathVariable int projectId,
            @PathVariable int taskId,
            @ModelAttribute TaskUpdateForm form,
            @RequestParam(name = "resourceFile", required = false) MultipartFile resourceFile,
            @RequestParam(name = "resourceNote", required = false) String resourceNote,
            Authentication auth,
            RedirectAttributes ra
    ) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();

        try {
            boardService.updateTaskFull(projectId, me.getUserId(), taskId, form);
            if (resourceFile != null && !resourceFile.isEmpty()) {
                resourceService.uploadForTask(projectId, taskId, me.getUserId(), resourceFile, resourceNote);
            }
            ra.addFlashAttribute("successMessage", "Cập nhật task & file thành công!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/projects/" + projectId + "#board-content";
    }
    
    @PostMapping("/projects/{projectId}/tasks/{taskId}/delete")
    @PreAuthorize("isAuthenticated()") 
    public String deleteTask(
            @PathVariable int projectId,
            @PathVariable int taskId,
            Authentication auth,
            RedirectAttributes ra
    ) {

        var me = userService.findByEmail(auth.getName()).orElseThrow();

        boolean isPm = boardService.isProjectPm(projectId, me.getUserId());
        if (!isPm) {
            ra.addFlashAttribute("errorMessage", "Bạn không có quyền xoá task.");
            return "redirect:/projects/" + projectId + "#board-content";
        }

        try {
            taskService.deleteTask(projectId, taskId);
            ra.addFlashAttribute("successMessage", "Xoá task thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/projects/" + projectId + "#board-content";
    }


    

}
