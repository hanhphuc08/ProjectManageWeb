package com.example.projectmanageweb.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.ResourceItemDto;
import com.example.projectmanageweb.model.ProjectResource;

@Repository
public class ProjectResourcesRepository {

	private final JdbcTemplate jdbc;

    public ProjectResourcesRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int insert(ProjectResource r) {
        String sql = """
            INSERT INTO project_resources
            (project_id, task_id, uploader_id,
             stored_name, original_name, content_type,
             size_bytes, description, uploaded_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbc.update(sql,
                r.getProjectId(),
                r.getTaskId(),
                r.getUploaderId(),
                r.getStoredName(),
                r.getOriginalName(),
                r.getContentType(),
                r.getSizeBytes(),
                r.getDescription(),
                r.getUploadedAt() != null ? r.getUploadedAt() : LocalDateTime.now()
        );

        // nếu cần id: SELECT LAST_INSERT_ID()
        Integer id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        return id != null ? id : 0;
    }

    public List<ResourceItemDto> findByProject(int projectId) {
        String sql = """
            SELECT
              r.resource_id,
              r.task_id,
              t.title       AS task_title,
              t.status      AS task_status,
              t.type        AS task_type,          -- dùng cột type trong tasks

              r.original_name,
              r.content_type,
              r.size_bytes,

              u.user_id     AS uploader_id,
              u.full_name   AS uploader_name,

              COALESCE(pr.role_name, pm.role_in_project) AS uploader_role,

              r.uploaded_at
            FROM project_resources r
            LEFT JOIN tasks t
                   ON t.task_id = r.task_id
            JOIN users u
                   ON u.user_id = r.uploader_id
            LEFT JOIN project_members pm
                   ON pm.project_id = r.project_id
                  AND pm.user_id   = r.uploader_id
            LEFT JOIN project_roles pr
                   ON pr.project_role_id = pm.project_role_id
            WHERE r.project_id = ?
            ORDER BY r.uploaded_at DESC, r.resource_id DESC
            """;

        return jdbc.query(sql, (rs, i) -> {
            ResourceItemDto dto = new ResourceItemDto();
            dto.setResourceId(rs.getInt("resource_id"));
            dto.setTaskId((Integer) rs.getObject("task_id"));
            dto.setTaskTitle(rs.getString("task_title"));
            dto.setTaskStatus(rs.getString("task_status"));
            dto.setTaskType(rs.getString("task_type"));

            dto.setOriginalName(rs.getString("original_name"));
            dto.setContentType(rs.getString("content_type"));
            dto.setSizeBytes(rs.getLong("size_bytes"));

            dto.setUploaderId((Integer) rs.getObject("uploader_id"));
            dto.setUploaderName(rs.getString("uploader_name"));
            dto.setUploaderRoleInProject(rs.getString("uploader_role"));

            dto.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
            return dto;
        }, projectId);
    }


    public ProjectResource findById(int resourceId) {
        String sql = """
            SELECT *
            FROM project_resources
            WHERE resource_id = ?
        """;

        List<ProjectResource> list = jdbc.query(sql, (rs, i) -> {
            ProjectResource r = new ProjectResource();
            r.setResourceId(rs.getInt("resource_id"));
            r.setProjectId(rs.getInt("project_id"));
            r.setTaskId((Integer) rs.getObject("task_id"));
            r.setUploaderId(rs.getInt("uploader_id"));
            r.setStoredName(rs.getString("stored_name"));
            r.setOriginalName(rs.getString("original_name"));
            r.setContentType(rs.getString("content_type"));
            r.setSizeBytes(rs.getLong("size_bytes"));
            r.setDescription(rs.getString("description"));
            r.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
            return r;
        }, resourceId);

        return list.isEmpty() ? null : list.get(0);
    }

    public void deleteById(int resourceId) {
        jdbc.update("DELETE FROM project_resources WHERE resource_id = ?", resourceId);
    }
}
