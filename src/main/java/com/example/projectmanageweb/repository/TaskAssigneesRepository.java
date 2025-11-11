package com.example.projectmanageweb.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.AssigneeDto;
import com.example.projectmanageweb.model.TaskAssignee;

@Repository
public class TaskAssigneesRepository {

	private final JdbcTemplate jdbc;

    public TaskAssigneesRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Lấy danh sách assign theo task
    public List<TaskAssignee> findByTask(int taskId) {
        String sql = """
            SELECT task_id, user_id
            FROM task_assignees
            WHERE task_id = ?
            """;

        return jdbc.query(sql, (rs, rowNum) -> {
            TaskAssignee a = new TaskAssignee();
            a.setTaskId(rs.getInt("task_id"));
            a.setUserId(rs.getInt("user_id"));
            return a;
        }, taskId);
    }

    public void addAssignee(int taskId, int userId) {
        String sql = """
            INSERT INTO task_assignees (task_id, user_id)
            VALUES (?, ?)
            """;
        jdbc.update(sql, taskId, userId);
    }
    
    public boolean isAssignee(int taskId, int userId) {
        String sql = """
                SELECT COUNT(*)
                FROM task_assignees
                WHERE task_id = ? AND user_id = ?
                """;
        Integer count = jdbc.queryForObject(sql, Integer.class, taskId, userId);
        return count != null && count > 0;
    }

    public List<Integer> findUserIdsByTask(int taskId) {
        String sql = "SELECT user_id FROM task_assignees WHERE task_id = ?";
        return jdbc.query(sql,
                (rs, rowNum) -> rs.getInt("user_id"),
                taskId);
    }
    
    public void removeAssignee(int taskId, int userId) {
        String sql = "DELETE FROM task_assignees WHERE task_id = ? AND user_id = ?";
        jdbc.update(sql, taskId, userId);
    }

    
}
