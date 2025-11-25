package com.example.projectmanageweb.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanageweb.dto.BoardViewDto;
import com.example.projectmanageweb.dto.MemberDto;
import com.example.projectmanageweb.dto.SuggestedAssignment;
import com.example.projectmanageweb.dto.TaskCardDto;
import com.example.projectmanageweb.dto.TaskCreateForm;
import com.example.projectmanageweb.dto.TaskUpdateForm;
import com.example.projectmanageweb.dto.board.BoardTaskItem;
import com.example.projectmanageweb.dto.board.ProjectBoardView;
import com.example.projectmanageweb.model.Task;
import com.example.projectmanageweb.repository.BoardRepository;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.ProjectsRepository;
import com.example.projectmanageweb.repository.TaskAssigneesRepository;
import com.example.projectmanageweb.repository.TasksRepository;
import com.example.projectmanageweb.repository.UserRepository;

@Service
public class BoardService {
	private final ProjectMembersRepository membersRepo;
    private final TasksRepository tasksRepo;
    private final TaskAssigneesRepository assigneesRepo;
    private final UserRepository usersRepo;
    private final ProjectsRepository projectsRepo;
    private final BoardRepository boardRepository;
    private final TasksRepository tasksRepository;
    private final ProjectMembersRepository projectMembersRepository;
    
	
	public BoardService(ProjectMembersRepository membersRepo, TasksRepository tasksRepo,
			TaskAssigneesRepository assigneesRepo, UserRepository usersRepo, ProjectsRepository projectsRepo,
			BoardRepository boardRepository, TasksRepository tasksRepository,
			ProjectMembersRepository projectMembersRepository) {
		super();
		this.membersRepo = membersRepo;
		this.tasksRepo = tasksRepo;
		this.assigneesRepo = assigneesRepo;
		this.usersRepo = usersRepo;
		this.projectsRepo = projectsRepo;
		this.boardRepository = boardRepository;
		this.tasksRepository = tasksRepository;
		this.projectMembersRepository = projectMembersRepository;
	}
	public BoardViewDto loadBoard(int projectId, int currentUserId) {

        BoardViewDto dto = new BoardViewDto();

        // 1. Load project
        var projectName = projectsRepo.findProjectNameById(projectId);
        dto.setProjectId(projectId);
        dto.setProjectName(projectName);

        // 2. Load members
        var members = membersRepo.findMembersByProject(projectId);

        List<MemberDto> memberDtos = members.stream().map(m -> {
            MemberDto md = new MemberDto();
            md.setProjectMemberId(m.getProjectMemberId());
            md.setUserId(m.getUserId());
            md.setRole(m.getRole());

            var user = usersRepo.findById(m.getUserId());
            md.setFullName(user.getFullName());

            var skills = membersRepo.findSkillsByProjectMember(m.getProjectMemberId());
            md.setSkills(skills.stream().map(s -> s.getSkillName()).toList());

            return md;
        }).toList();

        dto.setMembers(memberDtos);

        // 3. Xác định current user
        dto.setCurrentUser(
                memberDtos.stream()
                        .filter(m -> m.getUserId() == currentUserId)
                        .findFirst()
                        .orElse(null)
        );
        boolean isPmOfProject = membersRepo.isPmOfProject(projectId, currentUserId);
        dto.setCurrentUserIsPm(isPmOfProject);

        // 4. Load tasks
        var tasks = tasksRepo.findByProject(projectId);

        List<TaskCardDto> todo = new ArrayList<>();
        List<TaskCardDto> doing = new ArrayList<>();
        List<TaskCardDto> review=  new ArrayList<>();
        List<TaskCardDto> done = new ArrayList<>();

        for (Task t : tasks) {
            TaskCardDto card = new TaskCardDto();
            card.setTaskId(t.getTaskId());
            card.setSummary(t.getTitle());
            card.setStatus(t.getStatus());
            card.setPriority(t.getPriority());
            card.setType(t.getType());
            card.setDueDate(t.getDueDate() == null ? null : t.getDueDate().toString());
            card.setCreatedAt(t.getCreatedAt());
            card.setUpdatedAt(t.getUpdatedAt());

            // Lấy assignees
            var assigns = assigneesRepo.findByTask(t.getTaskId());

            List<String> assigneeNames = assigns.stream()
                    .map(a -> memberDtos.stream()
                            .filter(md -> md.getUserId() == a.getUserId())
                            .findFirst()
                            .map(MemberDto::getFullName)
                            .orElse("Unknown"))
                    .toList();

            card.setAssignees(assigneeNames);
            
            List<Integer> assigneeIds = assigns.stream()
                    .map(a -> a.getUserId())
                    .toList();
            card.setAssigneeIds(assigneeIds);
            
            //currentUser có phải assignee của task này không 
            boolean isAssignee = assigns.stream()
                    .anyMatch(a -> a.getUserId() == currentUserId);
            
            card.setCanChangeStatus(isPmOfProject || isAssignee);

            // chia cột
            switch (t.getStatus()) {
                case "To Do" -> todo.add(card);
                case "In Progress" -> doing.add(card);
                case "Review" -> review.add(card);
                case "Done" -> done.add(card);
                default -> {
                    
                    System.out.println("Unknown status = " + t.getStatus());
                }
            }

        }

        dto.setTodo(todo);
        dto.setInProgress(doing);
        dto.setReview(review);
        dto.setDone(done);

        return dto;
    }
	public void createTask(int projectId, int currentUserId, TaskCreateForm form) {

	    // 1. Chỉ PM mới được tạo task
	    if (!membersRepo.isPmOfProject(projectId, currentUserId)) {
	        throw new AccessDeniedException("Only PM can create tasks");
	    }

	    LocalDate due = null;
	    if (form.getDueDate() != null && !form.getDueDate().isBlank()) {
	        due = LocalDate.parse(form.getDueDate());
	    }

	    // 3. Insert task
	    int taskId = tasksRepo.createTask(
	            projectId,
	            currentUserId,
	            form.getTitle(),
	            form.getDescription(),
	            form.getPriority() == null || form.getPriority().isBlank()
	                    ? "Medium"
	                    : form.getPriority(),
	            due
	    );

	    // 4. Insert assignees
	    if (form.getAssigneeIds() != null) {
	        for (Integer uid : form.getAssigneeIds()) {
	            if (uid != null) {
	                assigneesRepo.addAssignee(taskId, uid);
	            }
	        }
	    }
	}
	
	public ProjectBoardView buildBoard(int projectId, Integer currentUserId) {

	    // 1. Lấy toàn bộ task của project
	    List<BoardTaskItem> tasks = boardRepository.findTasksByProjectId(projectId);
	    System.out.println("BUILD TIMELINE projectId=" + projectId
	            + " tasks.size=" + tasks.size());

	    // in thử 5 task đầu
	    tasks.stream().limit(5).forEach(t ->
	        System.out.println("taskId=" + t.getTaskId()
	            + " title=" + t.getSummary()
	            + " status=" + t.getStatus())
	    );

	    // 2. Lấy danh sách members
	    var members = boardRepository.findMembersByProjectId(projectId);

	    // 3. Check current user có phải PM không
	    boolean isPm = false;
	    if (currentUserId != null) {
	        isPm = boardRepository.isUserProjectManager(projectId, currentUserId);
	    }

	    // 4. Gán quyền đổi status cho từng task
	    if (currentUserId != null) {
	        for (BoardTaskItem t : tasks) {
	            boolean isAssignee = assigneesRepo.isAssignee(t.getTaskId(), currentUserId);
	            t.setCanChangeStatus(isPm || isAssignee);
	        }
	    } else {
	        // chưa login → không cho đổi
	        tasks.forEach(t -> t.setCanChangeStatus(false));
	    }

	    // 5. Nhóm theo status
	    List<BoardTaskItem> todo = tasks.stream()
	            .filter(t -> "To Do".equals(t.getStatus()))
	            .collect(Collectors.toList());

	    List<BoardTaskItem> inProgress = tasks.stream()
	            .filter(t -> "In Progress".equals(t.getStatus()))
	            .collect(Collectors.toList());

	    List<BoardTaskItem> review = tasks.stream()
	            .filter(t -> "Review".equals(t.getStatus()))
	            .collect(Collectors.toList());

	    List<BoardTaskItem> done = tasks.stream()
	            .filter(t -> "Done".equals(t.getStatus()))
	            .collect(Collectors.toList());

	    // 6. Build ProjectBoardView
	    ProjectBoardView board = new ProjectBoardView();
	    board.setProjectId(projectId);
	    board.setProjectName(boardRepository.findProjectName(projectId));
	    board.setCurrentUserIsPm(isPm);

	    board.setTodo(todo);
	    board.setInProgress(inProgress);
	    board.setReview(review);
	    board.setDone(done);

	    board.setMembers(members);

	    return board;
	}



	private String mapStatusCode(String code) {
	    if (code == null) return "To Do";
	    return switch (code) {
	        case "IN_PROGRESS" -> "In Progress";
	        case "REVIEW"      -> "Review";
	        case "DONE"        -> "Done";
	        case "TODO"        -> "To Do";
	        default            -> "To Do";
	    };
	}

	public void updateTaskStatusAndAssignees(int projectId, int currentUserId, int taskId, String statusCode,
			List<Integer> assigneeIds) {

		boolean isPm = membersRepo.isPmOfProject(projectId, currentUserId);
		boolean isAssignee = assigneesRepo.isAssignee(taskId, currentUserId);

		if (!isPm && !isAssignee) {
			throw new AccessDeniedException("Bạn không có quyền thay đổi trạng thái task này");
		}

		String statusDb = mapStatusCode(statusCode);
		tasksRepo.updateStatus(taskId, statusDb);
		
		if(assigneeIds == null || !isPm) {
			return;
		}
		List<Integer> current = assigneesRepo.findUserIdsByTask(taskId);
		
		Set<Integer> currentSet = new HashSet<>(current);
	    Set<Integer> newSet = assigneeIds.stream()
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());
		
	    for (Integer uid : newSet) {
	        if (!currentSet.contains(uid)) {
	            assigneesRepo.addAssignee(taskId, uid);
	        }
	    }
	    for (Integer uid : currentSet) {
	        if (!newSet.contains(uid)) {
	            assigneesRepo.removeAssignee(taskId, uid);
	        }
	    }
	}
	
	public void updateTaskFull(int projectId, int userId, int taskId, TaskUpdateForm form) {

	    boolean isPm = membersRepo.isPmOfProject(projectId, userId);
	    boolean isAssignee = assigneesRepo.isAssignee(taskId, userId);

	    if (!isPm && !isAssignee) {
	        throw new AccessDeniedException("Bạn không có quyền chỉnh sửa task này.");
	    }

	    tasksRepo.updateTaskFull(taskId, form);

	    // cập nhật assignees
	    if (isPm) {
 	       assigneesRepo.syncAssignees(taskId, form.getAssigneeIds());
	    }
	}
	
	@Transactional
	public void applyAssignments(int projectId, int pmUserId, List<SuggestedAssignment> assigns) {

	    if (!membersRepo.isPmOfProject(projectId, pmUserId)) {
	        throw new AccessDeniedException("Only PM can apply AI assignments.");
	    }

	    for (SuggestedAssignment a : assigns) {
	        if (a.getTaskId() == null || a.getAssigneeIds() == null) continue;
	        assigneesRepo.syncAssignees(a.getTaskId(), a.getAssigneeIds());
	    }
	}
	public void syncAssignees(int taskId, List<Integer> assigneeIds){
	    assigneesRepo.syncAssignees(taskId, assigneeIds);
	}
	
	public boolean isProjectPm(int projectId, int userId) {
	    return projectMembersRepository.isPmOfProject(projectId, userId);
	}




}
