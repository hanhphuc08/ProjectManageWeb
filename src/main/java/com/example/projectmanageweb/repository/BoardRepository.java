package com.example.projectmanageweb.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.board.BoardMemberItem;
import com.example.projectmanageweb.dto.board.BoardTaskItem;
import com.example.projectmanageweb.repository.rowmapper.BoardMemberItemRowMapper;
import com.example.projectmanageweb.repository.rowmapper.BoardTaskItemRowMapper;

@Repository
public class BoardRepository {
	
	private final JdbcTemplate jdbc;
    private final BoardTaskItemRowMapper taskRowMapper = new BoardTaskItemRowMapper();
    private final BoardMemberItemRowMapper memberRowMapper = new BoardMemberItemRowMapper();

    public BoardRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    public List<BoardTaskItem> findTasksByProjectId(int projectId) {
        String sql = """
            SELECT t.task_id,
                   t.title,
                   t.type,
                   t.status,
                   t.priority,
                   t.due_date
            FROM tasks t
            WHERE t.project_id = ?
            ORDER BY
                CASE t.status
                    WHEN 'To Do' THEN 1
                    WHEN 'In Progress' THEN 2
                    WHEN 'Review' THEN 3
                    WHEN 'Done' THEN 4
                    ELSE 5
                END,
                t.priority,
                t.due_date,
                t.created_at DESC
            """;
        return jdbc.query(sql, taskRowMapper, projectId);
    }
    
    public List<BoardMemberItem> findMembersByProjectId(int projectId) {
        String sql = """
            SELECT pm.project_member_id,
                   pm.user_id,
                   pm.role_in_project,
                   u.full_name,
                   u.avatar_url
            FROM project_members pm
            JOIN users u ON u.user_id = pm.user_id
            WHERE pm.project_id = ?
            ORDER BY
                CASE pm.role_in_project
                    WHEN 'PM' THEN 0
                    ELSE 1
                END,
                u.full_name
            """;
        return jdbc.query(sql, memberRowMapper, projectId);
    }

    public boolean isUserProjectManager(int projectId, int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM project_members
            WHERE project_id = ?
              AND user_id = ?
              AND role_in_project = 'PM'
            """;
        Integer count = jdbc.queryForObject(sql, Integer.class, projectId, userId);
        return count != null && count > 0;
    }

    public String findProjectName(int projectId) {
        String sql = "SELECT project_name FROM projects WHERE project_id = ?";
        return jdbc.queryForObject(sql, String.class, projectId);
    }

}
