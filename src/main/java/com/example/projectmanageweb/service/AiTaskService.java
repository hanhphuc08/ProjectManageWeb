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

    public AiTaskService(GroqService groqService,
                         ObjectMapper objectMapper,
                         AiMetadataRepository metadataRepository,
                         TasksRepository tasksRepository,
                         ProjectMembersRepository projectMembersRepository) {
        
        this.groqService = groqService;
        this.objectMapper = objectMapper;
        this.metadataRepository = metadataRepository;
        this.tasksRepository = tasksRepository;
        this.projectMembersRepository = projectMembersRepository;
    }

    /* =========================================================================
       JSON SAFETY PARSER — FIX AI TRẢ VỀ TEXT LÃNG XẸT
       ========================================================================= */
    private <T> T safeReadJsonArray(String raw, TypeReference<T> ref) throws Exception {
        if (raw == null) 
            throw new Exception("AI response is NULL");

        String trimmed = raw.trim();
        int start = trimmed.indexOf('[');
        int end = trimmed.lastIndexOf(']');

        if (start == -1 || end == -1 || end <= start) {
            throw new Exception("Không tìm thấy JSON trong response: " + trimmed);
        }

        String jsonArray = trimmed.substring(start, end + 1);
        return objectMapper.readValue(jsonArray, ref);
    }

    private String ns(String s) { return s == null ? "" : s; }

    private String escape(String s){
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }

    /* =========================================================================
       AI GỢI Ý TASK (KHÔNG DÙNG WBS)
       ========================================================================= */
    public List<SuggestedTask> suggestTasksForProject(Integer projectId, String noteFromPm) {

        ProjectSummary project = metadataRepository.findProjectSummary(projectId);
        List<Task> existingTasks = tasksRepository.findBasicByProject(projectId);

        // danh sách task cũ
        String existingTitles = existingTasks.stream()
                .map(t -> "- " + ns(t.getTitle()))
                .collect(Collectors.joining("\n"));

        String systemPrompt = """
            Bạn là AI trợ lý PM phân rã công việc cho dự án TLCN hệ thống quản lý dự án giống Jira/ClickUp.
            MỤC TIÊU:
            - Chỉ đề xuất task mới, không trùng.
            - Task phải cụ thể, code/test được.
            OUTPUT:
            - Chỉ trả về JSON array, không text ngoài JSON.
            FORMAT:
            [
              {
                "title": "...",
                "description": "...",
                "priority": "LOW | MEDIUM | HIGH",
                "estimateOptimistic": 1,
                "estimateLikely": 2,
                "estimatePessimistic": 3,
                "durationDays": 3
              }
            ]
            """;

        String userPrompt = """
            Thông tin dự án:
            - Tên: %s
            - Loại: %s
            - Mô tả: %s
            Task hiện có:
            %s

            Ghi chú từ PM:
            %s

            ➜ Hãy trả về đúng JSON array. Không text ngoài JSON.
            """.formatted(
                ns(project.getProjectName()),
                ns(project.getProjectTypeName()),
                ns(project.getDescription()),
                existingTitles,
                ns(noteFromPm)
        );

        try {
            String raw = groqService.chat(systemPrompt, userPrompt);
            return safeReadJsonArray(raw, new TypeReference<List<SuggestedTask>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String normalizePriority(String p) {
        if (p == null) return "MEDIUM";
        String up = p.trim().toUpperCase();
        if (up.startsWith("H")) return "HIGH";
        if (up.startsWith("L")) return "LOW";
        return "MEDIUM";
    }

    @Transactional
    public void saveSuggestedTasks(Integer projectId,
                                   List<SuggestedTask> suggestions,
                                   Integer createdBy) {

        if (suggestions == null || suggestions.isEmpty()) return;

        for (SuggestedTask s : suggestions) {
            if (s.getTitle() == null || s.getTitle().isBlank()) continue;

            String priority = normalizePriority(s.getPriority());

            LocalDate dueDate = null;
            Integer duration = s.getDurationDays();
            if (duration != null && duration > 0) {
                dueDate = LocalDate.now().plusDays(duration);
            }

            tasksRepository.createTask(projectId,
                    createdBy != null ? createdBy : 0,
                    s.getTitle(),
                    s.getDescription(),
                    priority,
                    dueDate);
        }
    }

    /* =========================================================================
       GỢI Ý ASSIGN TASK → MEMBER
       ========================================================================= */
    @Transactional(readOnly = true)
    public List<SuggestedAssignment> suggestAssignmentsForProject(Integer projectId, String note) {

        ProjectSummary project = metadataRepository.findProjectSummary(projectId);
        List<Task> tasks = tasksRepository.findUnassignedBasicByProject(projectId);
        if (tasks.isEmpty()) return Collections.emptyList();

        List<MemberSkillProfile> members = projectMembersRepository.findMemberProfiles(projectId);

        String systemPrompt = """
            Bạn là AI trợ lý Project Manager, nhiệm vụ:
            - Gợi ý thành viên phù hợp cho từng task.
            - OUTPUT: chỉ trả về JSON array DUY NHẤT.
            FORMAT:
            [
              {
                "taskId": 1,
                "assigneeIds": [2,3],
                "reason": "..."
              }
            ]
            """;

        String userPrompt = buildAssignUserPrompt(project, tasks, members, note);

        try {
            String raw = groqService.chat(systemPrompt, userPrompt);
            return safeReadJsonArray(raw, new TypeReference<List<SuggestedAssignment>>() {});
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
            - Tên: %s
            - Loại: %s
            - Mô tả: %s

            TASK:
            [%s]

            MEMBERS:
            [%s]

            Ghi chú PM:
            %s

            ➜ Chỉ trả về JSON array, không text khác.
        """.formatted(
                ns(project.getProjectName()),
                ns(project.getProjectTypeName()),
                ns(project.getDescription()),
                tasksJson,
                membersJson,
                ns(note)
        );
    }

    /* =========================================================================
       TÍNH ETA
       ========================================================================= */
    public int calculateRemainingDaysSequential(Integer projectId, List<SuggestedTask> newTasks) {

        List<Task> current = tasksRepository.findBasicByProject(projectId);

        double totalHours = 0;

        for (Task t : current) {
            if (!"Done".equalsIgnoreCase(t.getStatus())) {

                if (t.getDueDate() != null) {
                    long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), t.getDueDate());
                    totalHours += Math.max(daysLeft, 1) * 8.0;
                } else {
                    totalHours += 8.0;
                }
            }
        }

        for (SuggestedTask s : newTasks) {
            Integer d = s.getDurationDays();
            totalHours += (d != null && d > 0) ? d * 8.0 : 8.0;
        }

        return (int) Math.ceil(totalHours / 8.0);
    }

    public int calculateRemainingDaysParallel(Integer projectId, List<SuggestedTask> newTasks) {
        int members = projectMembersRepository.findMemberProfiles(projectId).size();
        if (members <= 0) members = 1;

        int seq = calculateRemainingDaysSequential(projectId, newTasks);

        return (int) Math.ceil(seq / (double) members);
    }

    @Transactional(readOnly = true)
    public AiSuggestResult suggestNewTasksAndEta(Integer projectId, String noteFromPm) {

        List<SuggestedTask> newTasks = suggestTasksForProject(projectId, noteFromPm);

        int seq = calculateRemainingDaysSequential(projectId, newTasks);
        int par = calculateRemainingDaysParallel(projectId, newTasks);

        return new AiSuggestResult(newTasks, seq, par);
    }
}
