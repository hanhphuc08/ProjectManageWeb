package com.example.projectmanageweb.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.dto.BoardViewDto;
import com.example.projectmanageweb.dto.ProjectCreateRequest;
import com.example.projectmanageweb.dto.ProjectMemberAddForm;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.ProjectTypesRepository;
import com.example.projectmanageweb.repository.ProjectsRepository;
import com.example.projectmanageweb.service.AuthService;
import com.example.projectmanageweb.service.BoardService;
import com.example.projectmanageweb.service.ProjectService;
import com.example.projectmanageweb.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProjectController {
	
	private final ProjectService projectsService;
	private final UserService userService;
	private final ProjectTypesRepository typesRepo;
	private final BoardService boardService;
	private final AuthService authService;
	private final ProjectMembersRepository membersRepo;

	
	public ProjectController(ProjectService projectsService, UserService userService, ProjectTypesRepository typesRepo,
			BoardService boardService, AuthService authService, ProjectMembersRepository membersRepo) {
		super();
		this.projectsService = projectsService;
		this.userService = userService;
		this.typesRepo = typesRepo;
		this.boardService = boardService;
		this.authService = authService;
		this.membersRepo = membersRepo;
	}
	@GetMapping("/projects")
	public String home(@RequestParam(required = false) String q, Authentication auth, Model model)
	{
		var email = auth.getName();
		var me = userService.findByEmail(email).orElseThrow();
		
		boolean admin = auth.getAuthorities().stream()
		     .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		
		model.addAttribute("projects",
		     projectsService.listForUser(me.getUserId(), q, admin));
		model.addAttribute("q", q);
		
		return "users/home";
	}
	@GetMapping("/user/home")
    public String legacyHomeRedirect() {
        return "redirect:/projects";
    }

	
	@GetMapping("/projects/create")
	@PreAuthorize("isAuthenticated()")
	  public String createForm(Model model) {
	    if (!model.containsAttribute("form"))
	    	model.addAttribute("form", new ProjectCreateRequest());
	    
	    model.addAttribute("types", typesRepo.findAll());
	    return "users/projectdashboard"; 
	  }
	
	
	@PostMapping("/projects")
    @PreAuthorize("isAuthenticated()")
    public String create(@Valid @ModelAttribute("form") ProjectCreateRequest form,
                         BindingResult br,
                         Authentication auth,
                         RedirectAttributes ra,
                         Model model) {

        if (br.hasErrors()) {
            model.addAttribute("types", typesRepo.findAll());
            return "users/projectdashboard";
        }

        var email = auth.getName();
        var me = userService.findByEmail(email).orElseThrow();

        // auto add creator làm PM của project trong service
        int pid = projectsService.create(form, me.getUserId());

        ra.addFlashAttribute("created", true);
        // Sau khi tạo xong quay lại danh sách projects (home)
        return "redirect:/projects";
    }
	
	@GetMapping("/projects/{projectId}")
	@PreAuthorize("isAuthenticated()")
	public String detail(@PathVariable int projectId,
	                     Authentication auth,
	                     Model model,
	                     RedirectAttributes ra) {

	    var email = auth.getName();
	    var me = userService.findByEmail(email).orElseThrow();
	    int currentUserId = me.getUserId();

	    boolean admin = auth.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	    var project = projectsService.getProjectDetail(projectId, me.getUserId(), admin);

	    if (project == null) {
	        ra.addFlashAttribute("error", "Project không tồn tại hoặc bạn không có quyền truy cập.");
	        return "redirect:/user/home";
	    }
	   
        BoardViewDto board = boardService.loadBoard(projectId, currentUserId);
        model.addAttribute("board", board);

	    model.addAttribute("project", project); 
	    
	    // ==== DỮ LIỆU CHO MODAL ADD MEMBER ====
	    var allUsers = authService.findAll(); 
	    var currentMembers = board.getMembers().stream()
	            .map(m -> m.getUserId())
	            .collect(java.util.stream.Collectors.toSet());

	    var availableUsers = allUsers.stream()
	            .filter(u -> !currentMembers.contains(u.getUserId()))
	            .toList();

	    model.addAttribute("availableUsers", availableUsers);
	    model.addAttribute("projectRoles", membersRepo.findProjectRoles(projectId));
	    model.addAttribute("memberForm", new ProjectMemberAddForm());
	    return "users/projectmanage";           
	}
	
	


	  
}
