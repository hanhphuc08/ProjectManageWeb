package com.example.projectmanageweb.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.ProjectSummary;
import com.example.projectmanageweb.dto.SuggestedTask;
import com.example.projectmanageweb.dto.WbsNodeSummary;
import com.example.projectmanageweb.repository.AiMetadataRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiTaskService {
	private final GroqService groqService;
    private final ObjectMapper objectMapper;
    private final AiMetadataRepository metadataRepository;

    public AiTaskService(GroqService groqService,
                         ObjectMapper objectMapper,
                         AiMetadataRepository metadataRepository) {
        this.groqService = groqService;
        this.objectMapper = objectMapper;
        this.metadataRepository = metadataRepository;
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
    "estimatePessimistic": 6
  }
]

QUY TẮC
- priority phải viết HOA: LOW / MEDIUM / HIGH.
- estimateOptimistic ≤ estimateLikely ≤ estimatePessimistic.
- Tất cả estimate là số nguyên (giờ).
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
	

}
