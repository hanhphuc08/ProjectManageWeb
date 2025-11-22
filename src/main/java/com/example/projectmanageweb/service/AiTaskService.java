package com.example.projectmanageweb.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<SuggestedTask> suggestTasksForProject(
            Integer projectId,
            String noteFromPm
    ) {
        // 1. Lấy thông tin dự án từ DB
        ProjectSummary project = metadataRepository.findProjectSummary(projectId);

        String projectName = project.getProjectName();
        String projectType = project.getProjectTypeName();
        String projectDesc = project.getDescription();

        String systemPrompt = """
                Bạn là trợ lý AI giúp Project Manager phân rã công việc cho một dự án phần mềm
                (website quản lý dự án giống Jira / ClickUp).

                MỤC TIÊU
                - Đề xuất danh sách các task triển khai cụ thể cho dự án.
                - Task phải mô tả công việc thật, có thể code được.
                - Đây là dự án tốt nghiệp của sinh viên (TLCN).

                YÊU CẦU VỀ ĐỊNH DẠNG
                - Chỉ trả về DUY NHẤT một mảng JSON hợp lệ.
                - Không giải thích, không markdown, không text bên ngoài JSON.

                Cấu trúc JSON:

                [
                  {
                    "title": "Tên task ngắn bằng tiếng Anh",
                    "description": "Mô tả chi tiết 1–3 câu (có thể tiếng Việt).",
                    "priority": "LOW | MEDIUM | HIGH",
                    "estimateOptimistic": 2,
                    "estimateLikely": 4,
                    "estimatePessimistic": 6,
                    "durationDays": 3
                  }
                ]

                QUY TẮC
                - priority phải viết HOA: LOW / MEDIUM / HIGH.
                - estimateOptimistic ≤ estimateLikely ≤ estimatePessimistic.
                - Tất cả estimate là số nguyên (giờ).
                - durationDays là số nguyên (ngày) > 0, phản ánh thời gian thực hiện task.
                - Không sinh `taskType`, mặc định luôn là "Task".
                - Số lượng task: 8–15.

                NGÔN NGỮ
                - Title: tiếng Anh.
                - Description: tiếng Việt hoặc Anh đều được nhưng phải dễ hiểu.
                """;

        String userPrompt = """
                Thông tin dự án:
                - Tên dự án: %s
                - Loại dự án: %s
                - Mô tả: %s

                Ghi chú thêm từ PM:
                %s

                Hãy đề xuất danh sách các task triển khai cho dự án,
                tuân theo đúng cấu trúc JSON đã quy định ở trên.
                Chỉ trả về JSON, không thêm giải thích.
                """
                .formatted(
                        ns(projectName),
                        ns(projectType),
                        ns(projectDesc),
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
    	List<Task> tasks = tasksRepository.findBasicByProject(projectId);
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

	

}
