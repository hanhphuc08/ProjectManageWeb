package com.example.projectmanageweb.repository;

import java.beans.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.TaskCardDto;
import com.example.projectmanageweb.model.Task;

@Repository
public class TasksRepository {

	private final JdbcTemplate jdbc;

	public TasksRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	// Lấy tất cả task theo project
	public List<Task> findByProject(int projectId) {
		String sql = """
				    SELECT *
				    FROM tasks
				    WHERE project_id = ?
				    ORDER BY task_id DESC
				""";

		return jdbc.query(sql, (rs, i) -> {
			Task t = new Task();
			t.setTaskId(rs.getInt("task_id"));
			t.setProjectId(rs.getInt("project_id"));
			t.setTitle(rs.getString("title"));
			t.setDescription(rs.getString("description"));
			t.setStatus(rs.getString("status"));
			t.setPriority(rs.getString("priority"));
			t.setType(rs.getString("type"));
			t.setDueDate(rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null);
			return t;
		}, projectId);
	}

	public Task findById(int id) {
		String sql = "SELECT * FROM tasks WHERE task_id = ?";
		List<Task> list = jdbc.query(sql, (rs, i) -> {
			Task t = new Task();
			t.setTaskId(rs.getInt("task_id"));
			t.setProjectId(rs.getInt("project_id"));
			t.setTitle(rs.getString("title"));
			t.setDescription(rs.getString("description"));
			t.setStatus(rs.getString("status"));
			t.setPriority(rs.getString("priority"));
			t.setType(rs.getString("type"));
			return t;
		}, id);
		return list.isEmpty() ? null : list.get(0);
	}

	public int createTask(int projectId, int createdBy, String title, String description, String priority,
			LocalDate dueDate) {
		String sql = """
				INSERT INTO tasks (project_id, title, description, priority, due_date, created_by)
				VALUES (?, ?, ?, ?, ?, ?)
				""";
		KeyHolder kh = new GeneratedKeyHolder();

		jdbc.update(con -> {
			PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, projectId);
			ps.setString(2, title);
			ps.setString(3, description);
			ps.setString(4, priority);

			if (dueDate != null) {
				ps.setDate(5, Date.valueOf(dueDate));
			} else {
				ps.setNull(5, Types.DATE);
			}

			ps.setInt(6, createdBy);
			return ps;
		}, kh);

		Number key = kh.getKey();
		return key == null ? 0 : key.intValue();
	}
	
	public void updateStatus(int taskId, String statusDb) {
	    String sql = """
	            UPDATE tasks
	            SET status = ?
	            WHERE task_id = ?
	            """;
	    jdbc.update(sql, statusDb, taskId);
	}


}
