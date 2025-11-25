package com.example.projectmanageweb.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanageweb.dto.AiSuggestResult;
import com.example.projectmanageweb.dto.MemberSkillProfile;
import com.example.projectmanageweb.dto.ProjectSummary;
import com.example.projectmanageweb.dto.SuggestedAssignment;
import com.example.projectmanageweb.dto.SuggestedTask;
import com.example.projectmanageweb.dto.WbsNodeSummary;
import com.example.projectmanageweb.model.Task;
import com.example.projectmanageweb.repository.AiMetadataRepository;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.TasksRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiTaskService {
	private final GroqService groqService;
    private final ObjectMapper objectMapper;
    private final AiMetadataRepository metadataRepository;
    private final TasksRepository tasksRepository;
    private final ProjectMembersRepository projectMembersRepository;


    public AiTaskService(GroqService groqService, ObjectMapper objectMapper, AiMetadataRepository metadataRepository,
			TasksRepository tasksRepository, ProjectMembersRepository projectMembersRepository) {
		super();
		this.groqService = groqService;
		this.objectMapper = objectMapper;
		this.metadataRepository = metadataRepository;
		this.tasksRepository = tasksRepository;
		this.projectMembersRepository = projectMembersRepository;
	}

	// 🌟 Gợi ý task cho cả project (chưa dùng WBS)
    public List<SuggestedTask> suggestTasksForProject(Integer projectId, String noteFromPm) {

        ProjectSummary project = metadataRepository.findProjectSummary(projectId);
        List<Task> existingTasks = tasksRepository.findBasicByProject(projectId);

        // gom title cũ cho AI nhìn thấy
        String existingTitles = existingTasks.stream()
            .map(t -> "- " + ns(t.getTitle()))
            .collect(Collectors.joining("\n"));

        String systemPrompt = """
    Bạn là AI trợ lý PM phân rã công việc cho dự án TLCN hệ thống quản lý dự án giống Jira/ClickUp.
    Tech stack: Spring Boot + Thymeleaf + JDBC + MySQL.

    MỤC TIÊU
    - Chỉ đề xuất các task mới còn thiếu.
    - TUYỆT ĐỐI KHÔNG lặp lại hoặc tương tự các task đã có.
    - Task phải cụ thể, code/test được.

    OUTPUT
    - Chỉ trả về 1 mảng JSON hợp lệ, không text ngoài JSON.

    FORMAT
    [
      {
        "title": "Short English title <= 8 words",
        "description": "1–3 câu mô tả rõ việc cần làm",
        "priority": "LOW | MEDIUM | HIGH",
        "estimateOptimistic": 2,
        "estimateLikely": 4,
        "estimatePessimistic": 6,
        "durationDays": 3
      }
    ]

    RULES
    - Không sinh task trùng/na ná task đã có.
    - priority viết HOA.
    - estimateOptimistic ≤ estimateLikely ≤ estimatePessimistic (giờ).
    - durationDays: 1–5 ngày.
    - Số lượng task mới: 5–12.
    """;

        String userPrompt = """
    Thông tin dự án:
    - Tên: %s
    - Loại: %s
    - Mô tả: %s

    Danh sách task HIỆN CÓ trong dự án (KHÔNG ĐƯỢC LẶP LẠI):
    %s

    Ghi chú thêm từ PM:
    %s

    Hãy đề xuất danh sách task MỚI còn thiếu (không lặp lại task trên),
    theo đúng JSON format. Chỉ trả về JSON.
    """.formatted(
            ns(project.getProjectName()),
            ns(project.getProjectTypeName()),
            ns(project.getDescription()),
            existingTitles,
            ns(noteFromPm)
        );

        try {
            String json = groqService.chat(systemPrompt, userPrompt);
            return objectMapper.readValue(json, new TypeReference<List<SuggestedTask>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    private String ns(String s) { return s == null ? "" : s; }
    
    @Transactional
    public void saveSuggestedTasks(Integer projectId,
            List<SuggestedTask> suggestions,
            Integer createdBy) {
		if (suggestions == null || suggestions.isEmpty())
			return;

		for (SuggestedTask s : suggestions) {
			if (s.getTitle() == null || s.getTitle().isBlank()) {
				continue;
			}

			String priority = normalizePriority(s.getPriority());

			LocalDate dueDate = null;
			Integer duration = s.getDurationDays();
			if (duration != null && duration > 0) {
				dueDate = LocalDate.now().plusDays(duration);
			}

			tasksRepository.createTask(projectId, createdBy != null ? createdBy : 0,
					s.getTitle(), s.getDescription(), priority, dueDate);
		}
    }

    private String normalizePriority(String p) {
        if (p == null) return "MEDIUM";
        String up = p.trim().toUpperCase();
        if (up.startsWith("H")) return "HIGH";
        if (up.startsWith("L")) return "LOW";
        return "MEDIUM";
    }
    
    
    @Transactional(readOnly = true)
    public List<SuggestedAssignment> suggestAssignmentsForProject(Integer projectId, String note) {

    	ProjectSummary project = metadataRepository.findProjectSummary(projectId);
    	List<Task> tasks = tasksRepository.findUnassignedBasicByProject(projectId);
    	if (tasks.isEmpty()) {
    	    return Collections.emptyList();
    	}
    	List<MemberSkillProfile> members = projectMembersRepository.findMemberProfiles(projectId);
        

    	String systemPrompt = """
    		    Bạn là AI trợ lý Project Manager. Nhiệm vụ của bạn là gợi ý
    		    thành viên phù hợp cho từng task dựa trên kỹ năng và mức độ bận rộn.

    		    🔥 YÊU CẦU OUTPUT:
    		    - Chỉ trả về **DUY NHẤT một mảng JSON** hợp lệ.
    		    - Không viết markdown, không viết giải thích bên ngoài JSON.

    		    🔥 CẤU TRÚC JSON TRẢ VỀ:
    		    [
    		      {
    		        "taskId": 123,
    		        "assigneeIds": [2, 5],
    		        "reason": "Tóm tắt lý do chọn các thành viên: skill phù hợp, ít bận, kinh nghiệm,..."
    		      }
    		    ]

    		    🔥 QUY TẮC:
    		    - Không trả về trường confidence.
    		    - Luôn trả về trường "reason".
    		    - Lý do phải mô tả rõ ràng tại sao những assignee này phù hợp:
    		      + trùng kỹ năng với title/description của task
    		      + allocationPct thấp → rảnh hơn
    		      + availability = FULL_TIME/MANUAL
    		      + nếu skill yếu nhưng bận rộn thấp → vẫn có thể xem xét
    		    - Mỗi task có 1–3 assignee.
    		    - Không chọn user nếu dự án không có member nào.
    		    """;


        String userPrompt = buildAssignUserPrompt(project, tasks, members, note);

        try {
            String json = groqService.chat(systemPrompt, userPrompt);
            return objectMapper.readValue(json, new TypeReference<List<SuggestedAssignment>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String buildAssignUserPrompt(ProjectSummary project, List<Task> tasks,
                                         List<MemberSkillProfile> members, String note) {

        String tasksJson = tasks.stream().map(t -> """
          {
            "taskId": %d,
            "title": "%s",
            "description": "%s",
            "priority": "%s",
            "type": "%s"
          }
        """.formatted(
                t.getTaskId(),
                escape(t.getTitle()),
                escape(t.getDescription()),
                escape(t.getPriority()),
                escape(t.getType())
        )).collect(Collectors.joining(",\n"));

        String membersJson = members.stream().map(m -> """
          {
            "userId": %d,
            "fullName": "%s",
            "skills": %s,
            "allocationPct": %d,
            "availability": "%s"
          }
        """.formatted(
                m.getUserId(),
                escape(m.getFullName()),
                objectMapper.valueToTree(m.getSkills()).toString(),
                m.getAllocationPct() == null ? 100 : m.getAllocationPct(),
                escape(m.getAvailability())
        )).collect(Collectors.joining(",\n"));

        return """
		    Thông tin dự án:
		    - Tên dự án: %s
		    - Loại: %s
		    - Mô tả: %s
		
		    Danh sách TASK:
		    [%s]
		
		    Danh sách MEMBERS:
		    [%s]
		
		    Ghi chú thêm từ PM:
		    %s
		
		    Hãy gợi ý phân công member cho từng task theo đúng JSON format.
		    Chỉ trả về JSON.
    """.formatted(
                ns(project.getProjectName()),
                ns(project.getProjectTypeName()),
                ns(project.getDescription()),
                tasksJson,
                membersJson,
                ns(note)
        );
    }

    private String escape(String s){
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
    

    public int calculateRemainingDaysSequential(Integer projectId, List<SuggestedTask> newTasks) {

        List<Task> current = tasksRepository.findBasicByProject(projectId);

        double totalHours = 0;

        // 1) Task hiện có chưa Done
        for (Task t : current) {
            if (!"Done".equalsIgnoreCase(t.getStatus())) {

                if (t.getDueDate() != null) {
                    long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), t.getDueDate());
                    totalHours += Math.max(daysLeft, 1) * 8.0;
                } else {
                    totalHours += 8.0; // fallback: 1 ngày
                }
            }
        }

        // 2) Task mới AI
        for (SuggestedTask s : newTasks) {
            Integer d = s.getDurationDays();
            if (d != null && d > 0) {
                totalHours += d * 8.0;
            } else {
                totalHours += 8.0;
            }
        }

        return (int) Math.ceil(totalHours / 8.0);
    }

    public int calculateRemainingDaysParallel(Integer projectId, List<SuggestedTask> newTasks) {
        int members = projectMembersRepository.findMemberProfiles(projectId).size();
        if (members <= 0) members = 1;

        int sequentialDays = calculateRemainingDaysSequential(projectId, newTasks);

        return (int) Math.ceil(sequentialDays / (double) members);
    }
    
    @Transactional(readOnly = true)
    public AiSuggestResult suggestNewTasksAndEta(Integer projectId, String noteFromPm) {

        List<SuggestedTask> newTasks = suggestTasksForProject(projectId, noteFromPm);

        int seq = calculateRemainingDaysSequential(projectId, newTasks);
        int par = calculateRemainingDaysParallel(projectId, newTasks);

        return new AiSuggestResult(newTasks, seq, par);
    }
    
    


	

}
