package com.example.projectmanageweb.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.model.TaskFile;

@Repository
public class TaskFilesRepository {
	
	private final JdbcTemplate jdbc;

    public TaskFilesRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int insert(TaskFile f) {
        String sql = """
            INSERT INTO task_files
              (project_id, task_id, uploader_id,
               original_name, stored_name, content_type, size_bytes, uploaded_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder kh = new GeneratedKeyHolder();

        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, f.getProjectId());
            ps.setInt(2, f.getTaskId());
            ps.setInt(3, f.getUploaderId());
            ps.setString(4, f.getOriginalName());
            ps.setString(5, f.getStoredName());
            ps.setString(6, f.getContentType());
            ps.setLong(7, f.getSize() == null ? 0L : f.getSize());
            ps.setTimestamp(8, Timestamp.valueOf(f.getUploadedAt()));
            return ps;
        }, kh);

        Number key = kh.getKey();
        return key == null ? 0 : key.intValue();
    }

    public List<TaskFile> findByTask(int projectId, int taskId) {
        String sql = """
            SELECT file_id, project_id, task_id, uploader_id,
                   original_name, stored_name, content_type, size_bytes, uploaded_at
            FROM task_files
            WHERE project_id = ? AND task_id = ?
            ORDER BY uploaded_at DESC, file_id DESC
            """;

        return jdbc.query(sql, (rs, i) -> {
            TaskFile f = new TaskFile();
            f.setFileId(rs.getInt("file_id"));
            f.setProjectId(rs.getInt("project_id"));
            f.setTaskId(rs.getInt("task_id"));
            f.setUploaderId(rs.getInt("uploader_id"));
            f.setOriginalName(rs.getString("original_name"));
            f.setStoredName(rs.getString("stored_name"));
            f.setContentType(rs.getString("content_type"));
            f.setSize(rs.getLong("size_bytes"));
            f.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
            return f;
        }, projectId, taskId);
    }

    public List<TaskFile> findByProject(int projectId) {
        String sql = """
            SELECT file_id, project_id, task_id, uploader_id,
                   original_name, stored_name, content_type, size_bytes, uploaded_at
            FROM task_files
            WHERE project_id = ?
            ORDER BY uploaded_at DESC, file_id DESC
            """;

        return jdbc.query(sql, (rs, i) -> {
            TaskFile f = new TaskFile();
            f.setFileId(rs.getInt("file_id"));
            f.setProjectId(rs.getInt("project_id"));
            f.setTaskId(rs.getInt("task_id"));
            f.setUploaderId(rs.getInt("uploader_id"));
            f.setOriginalName(rs.getString("original_name"));
            f.setStoredName(rs.getString("stored_name"));
            f.setContentType(rs.getString("content_type"));
            f.setSize(rs.getLong("size_bytes"));
            f.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
            return f;
        }, projectId);
    }

    public TaskFile findById(int fileId) {
        String sql = """
            SELECT file_id, project_id, task_id, uploader_id,
                   original_name, stored_name, content_type, size_bytes, uploaded_at
            FROM task_files
            WHERE file_id = ?
            """;

        List<TaskFile> list = jdbc.query(sql, (rs, i) -> {
            TaskFile f = new TaskFile();
            f.setFileId(rs.getInt("file_id"));
            f.setProjectId(rs.getInt("project_id"));
            f.setTaskId(rs.getInt("task_id"));
            f.setUploaderId(rs.getInt("uploader_id"));
            f.setOriginalName(rs.getString("original_name"));
            f.setStoredName(rs.getString("stored_name"));
            f.setContentType(rs.getString("content_type"));
            f.setSize(rs.getLong("size_bytes"));
            f.setUploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime());
            return f;
        }, fileId);

        return list.isEmpty() ? null : list.get(0);
    }

}
