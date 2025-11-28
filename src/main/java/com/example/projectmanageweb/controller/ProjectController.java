package com.example.projectmanageweb.controller;

import java.nio.file.attribute.UserPrincipal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.example.projectmanageweb.dto.ProjectResourcesView;
import com.example.projectmanageweb.dto.ResourceItemDto;
import com.example.projectmanageweb.dto.TaskResourceGroupDto;
import com.example.projectmanageweb.dto.UserResourceGroupDto;
import com.example.projectmanageweb.dto.calendar.CalendarViewDto;
import com.example.projectmanageweb.service.*;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.ProjectTypesRepository;
import com.example.projectmanageweb.repository.ProjectsRepository;
import com.example.projectmanageweb.repository.TasksRepository;
import com.example.projectmanageweb.service.AuthService;
import com.example.projectmanageweb.service.BoardService;
import com.example.projectmanageweb.service.CalendarService;
import com.example.projectmanageweb.service.GoalService;
import com.example.projectmanageweb.service.ProjectService;
import com.example.projectmanageweb.service.ProjectSummaryService;
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
	private final ProjectSummaryService projectSummaryService;
	private final CalendarService calendarService;
	private final GoalService goalService;
	private final TasksRepository tasksRepository;
	private final ResourceService resourceService;
	
	
	public ProjectController(ProjectService projectsService, UserService userService, ProjectTypesRepository typesRepo,
			BoardService boardService, AuthService authService, ProjectMembersRepository membersRepo,
			ProjectSummaryService projectSummaryService, CalendarService calendarService, GoalService goalService,
			TasksRepository tasksRepository, ResourceService resourceService) {
		super();
		this.projectsService = projectsService;
		this.userService = userService;
		this.typesRepo = typesRepo;
		this.boardService = boardService;
		this.authService = authService;
		this.membersRepo = membersRepo;
		this.projectSummaryService = projectSummaryService;
		this.calendarService = calendarService;
		this.goalService = goalService;
		this.tasksRepository = tasksRepository;
		this.resourceService = resourceService;
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
	    var summary = projectSummaryService.buildSummary(projectId);
	   
        BoardViewDto board = boardService.loadBoard(projectId, currentUserId);
        model.addAttribute("board", board);

	    model.addAttribute("project", project); 
	    model.addAttribute("summary", summary);
	    
	    // ==== DỮ LIỆU CHO MODAL ADD MEMBER ====
	    var allUsers = authService.findAll(); 
	    var currentMembers = board.getMembers().stream()
	            .map(m -> m.getUserId())
	            .collect(java.util.stream.Collectors.toSet());

	    var availableUsers = allUsers.stream()
	            .filter(u -> !currentMembers.contains(u.getUserId()))
	            .toList();
	    
	    var goals = goalService.listGoals(projectId);
	    
	    
	    model.addAttribute("goals", goals);
	    model.addAttribute("projectTasks", tasksRepository.findBasicByProject(projectId));
	    
	    model.addAttribute("availableUsers", availableUsers);
	    model.addAttribute("projectRoles", membersRepo.findProjectRoles(projectId));
	    model.addAttribute("memberForm", new ProjectMemberAddForm());
	    
	    CalendarViewDto calendar = calendarService.build(projectId, null);
	    model.addAttribute("calendar", calendar);
	    model.addAttribute("currentMonthLabel",
	            calendarService.getMonthLabel(calendar.getYear(), calendar.getMonth()));
	    
	    
	    List<ResourceItemDto> files = resourceService.listByProject(projectId);

	    ProjectResourcesView resView = new ProjectResourcesView();
	    resView.setAllFiles(files);
	    resView.setTotalFiles(files.size());

	    long totalSize = files.stream()
	            .mapToLong(ResourceItemDto::getSizeBytes)
	            .sum();
	    resView.setTotalSizeBytes(totalSize);

	    // ===== Group theo TASK =====
	    Map<Integer, List<ResourceItemDto>> mapByTask =
	            files.stream()
	                 .collect(Collectors.groupingBy(ResourceItemDto::getTaskId));

	    List<TaskResourceGroupDto> taskGroups = mapByTask.entrySet().stream()
	            .map(e -> {
	                int taskId = e.getKey();
	                List<ResourceItemDto> taskFiles = e.getValue();
	                ResourceItemDto first = taskFiles.get(0);

	                TaskResourceGroupDto g = new TaskResourceGroupDto();
	                g.setTaskId(taskId);
	                g.setTaskTitle(first.getTaskTitle());
	                g.setStatus(first.getTaskStatus());
	                g.setType(first.getTaskType());
	                g.setFiles(taskFiles);
	                return g;
	            })
	            .sorted(Comparator.comparing(TaskResourceGroupDto::getTaskId))
	            .toList();

	    resView.setByTask(taskGroups);

	    // ===== Group theo USER =====
	    // yêu cầu ResourceItemDto có uploaderId, uploaderName, uploaderRoleInProject
	    Map<Integer, List<ResourceItemDto>> mapByUser =
	            files.stream()
	                 .filter(f -> f.getUploaderId() != null)
	                 .collect(Collectors.groupingBy(ResourceItemDto::getUploaderId));

	    List<UserResourceGroupDto> userGroups = mapByUser.entrySet().stream()
	            .map(e -> {
	                List<ResourceItemDto> userFiles = e.getValue();
	                ResourceItemDto first = userFiles.get(0);

	                UserResourceGroupDto u = new UserResourceGroupDto();
	                u.setUserId(first.getUploaderId());
	                u.setFullName(first.getUploaderName());
	                u.setRoleInProject(first.getUploaderRoleInProject());
	                u.setFiles(userFiles);
	                return u;
	            })
	            .sorted(Comparator.comparing(
	                    UserResourceGroupDto::getFullName,
	                    Comparator.nullsLast(String::compareToIgnoreCase)
	            ))
	            .toList();

	    resView.setByUser(userGroups);

	    model.addAttribute("resources", resView);
	    
	    return "users/projectmanage";           
	}
	
	
	// ===== Fragment Timeline (load khi bấm tab) =====
	@GetMapping("/projects/{projectId}/timeline")
	@PreAuthorize("isAuthenticated()")
	public String timelineFragment(@PathVariable int projectId,
	                               Authentication auth,
	                               Model model) {

	    var email = auth.getName();
	    var me = userService.findByEmail(email).orElseThrow();
	    int currentUserId = me.getUserId();

	    model.addAttribute("board",
	            boardService.buildBoard(projectId, currentUserId));

	    return "users/project/fragments/timeline";
	}
	
	@GetMapping("/projects/{projectId}/calendar")
	public String projectCalendar(
	        @PathVariable int projectId,
	        @RequestParam(required = false) String month,
	        Model model
	) {
	    CalendarViewDto calendar = calendarService.build(projectId, month);

	    model.addAttribute("projectId", projectId);
	    model.addAttribute("calendar", calendar);

	    model.addAttribute("currentMonthLabel",
	            calendarService.getMonthLabel(calendar.getYear(), calendar.getMonth())
	    );

	    return "users/project/fragments/calendar";  
	}
	
	@GetMapping("/projects/{projectId}/goals")
	@PreAuthorize("isAuthenticated()")
	public String goalsFragment(@PathVariable int projectId,
	                            org.springframework.ui.Model model) {

	    var goals = goalService.listGoals(projectId);
	    model.addAttribute("goals", goals);
	    model.addAttribute("projectId", projectId);

	    return "users/project/fragments/goals";
	}
	

  
}
