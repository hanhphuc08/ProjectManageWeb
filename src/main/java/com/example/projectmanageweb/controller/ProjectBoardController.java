package com.example.projectmanageweb.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.dto.BoardViewDto;
import com.example.projectmanageweb.dto.TaskCreateForm;
import com.example.projectmanageweb.service.BoardService;
import com.example.projectmanageweb.service.UserService;

@Controller
@RequestMapping("/projects")
public class ProjectBoardController {

	 private final BoardService boardService;
	 private final UserService userService;

	
	public ProjectBoardController(BoardService boardService, UserService userService) {
		super();
		this.boardService = boardService;
		this.userService = userService;
	}


	@PostMapping("/{projectId}/tasks")
	public String createTask(@PathVariable int projectId,
	                         Authentication auth,
	                         TaskCreateForm form,
	                         RedirectAttributes ra) {

		var email = auth.getName();
	    var me = userService.findByEmail(email).orElseThrow();
	    int currentUserId = me.getUserId();

	    try {
	        boardService.createTask(projectId, currentUserId, form);
	        ra.addFlashAttribute("successMessage", "Task created successfully.");
	    } catch (AccessDeniedException ex) {
	        ra.addFlashAttribute("errorMessage", "You are not PM of this project.");
	    } catch (Exception ex) {
	        ra.addFlashAttribute("errorMessage", "Error while creating task.");
	    }

	    return "redirect:/projects/" + projectId;
	}


	    
}
