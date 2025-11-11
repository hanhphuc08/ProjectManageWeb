package com.example.projectmanageweb.controller;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.dto.ProjectMemberAddForm;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.service.AuthService;
import com.example.projectmanageweb.service.ProjectMembersService;
import com.example.projectmanageweb.service.UserService;



@Controller
@RequestMapping("/projects/{projectId}/members")
public class ProjectMemberController {
	private final ProjectMembersService membersService;
    private final UserService userService;
    private final ProjectMembersRepository membersRepo;
    private final AuthService authService;
	public ProjectMemberController(ProjectMembersService membersService, UserService userService,
			ProjectMembersRepository membersRepo, AuthService authService) {
		super();
		this.membersService = membersService;
		this.userService = userService;
		this.membersRepo = membersRepo;
		this.authService = authService;
	}
    
	@GetMapping("/add")
    public String showAddForm(@PathVariable int projectId,
                              Authentication auth,
                              Model model) {

        var me = userService.findByEmail(auth.getName()).orElseThrow();

        // danh sách user chưa nằm trong project
        var allUsers = authService.findAll();
        var currentMembers = membersRepo.findMembersByProject(projectId)
                                        .stream()
                                        .map(m -> m.getUserId())
                                        .collect(Collectors.toSet());
        var availableUsers = allUsers.stream()
                .filter(u -> !currentMembers.contains(u.getUserId()))
                .toList();

        model.addAttribute("projectId", projectId);
        model.addAttribute("availableUsers", availableUsers);
        model.addAttribute("projectRoles", membersRepo.findProjectRoles(projectId));
        model.addAttribute("form", new ProjectMemberAddForm());

        return "users/project/member-add";  
    }

    @PostMapping("/add")
    public String handleAdd(@PathVariable int projectId,
                            @ModelAttribute ProjectMemberAddForm form,
                            Authentication auth,
                            RedirectAttributes ra) {

        var me = userService.findByEmail(auth.getName()).orElseThrow();

        try {
            membersService.addMemberWithSkills(projectId, me.getUserId(), form);
            ra.addFlashAttribute("successMessage", "Thêm thành viên vào dự án thành công!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/projects/" + projectId + "#board-content";
    }
    @PostMapping("/{projectMemberId}/remove")
    public String removeMember(@PathVariable int projectId,
                               @PathVariable int projectMemberId,
                               Authentication auth,
                               RedirectAttributes ra) {

        var me = userService.findByEmail(auth.getName()).orElseThrow();

        try {
            membersService.removeMember(projectId, me.getUserId(), projectMemberId);
            ra.addFlashAttribute("successMessage", "Đã xóa thành viên khỏi dự án.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/projects/" + projectId + "#board-content";
    }
	
}
