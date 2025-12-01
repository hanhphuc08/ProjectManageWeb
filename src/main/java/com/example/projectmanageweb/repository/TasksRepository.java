package com.example.projectmanageweb.repository;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.CountDto;
import com.example.projectmanageweb.dto.LabelCountDto;
import com.example.projectmanageweb.dto.MemberTaskItem;
import com.example.projectmanageweb.dto.MetricDto;
import com.example.projectmanageweb.dto.RecentActivityDto;
import com.example.projectmanageweb.dto.TaskBasic;
import com.example.projectmanageweb.dto.TaskCardDto;
import com.example.projectmanageweb.dto.TaskUpdateForm;
import com.example.projectmanageweb.model.Task;
import com.example.projectmanageweb.repository.rowmapper.MemberTaskItemRowMapper;

@Repository
public class TasksRepository {

	private final JdbcTemplate jdbc;
	 private final MemberTaskItemRowMapper memberTaskItemRowMapper = new MemberTaskItemRowMapper();

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
			t.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
	        t.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
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
			t.setDueDate(rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null);
	        t.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
	        t.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
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
	public void updateTaskFull(int taskId, TaskUpdateForm f) {
	    String sql = """
	        UPDATE tasks 
	        SET title=?, description=?, priority=?, due_date=?, status=?
	        WHERE task_id=?
	    """;

	    LocalDate due = null;
	    if (f.getDueDate() != null && !f.getDueDate().isBlank())
	        due = LocalDate.parse(f.getDueDate());

	    jdbc.update(sql,
	            f.getTitle(),
	            f.getDescription(),
	            f.getPriority(),
	            due,
	            mapStatus(f.getStatus()),
	            taskId
	    );
	}
	
	private String mapStatus(String code) {
	    if (code == null) return "To Do";

	    return switch (code) {
	        case "IN_PROGRESS" -> "In Progress";
	        case "REVIEW"      -> "Review";
	        case "DONE"        -> "Done";
	        case "TODO"        -> "To Do";
	        default            -> "To Do"; 
	    };
	}
	
	public List<Task> findBasicByProject(int projectId) {
	    String sql = """
	        SELECT task_id, title, description, priority, status, type, due_date
	        FROM tasks
	        WHERE project_id = ?
	        ORDER BY task_id ASC
	    """;
	    return jdbc.query(sql, (rs, i) -> {
	        Task t = new Task();
	        t.setTaskId(rs.getInt("task_id"));
	        t.setTitle(rs.getString("title"));
	        t.setDescription(rs.getString("description"));
	        t.setPriority(rs.getString("priority"));
	        t.setStatus(rs.getString("status"));
	        t.setType(rs.getString("type"));
	        t.setDueDate(rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null);
			/* t.setCreatedAt(rs.getObject("created_at", LocalDateTime.class)); */
			/* t.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class)); */
	        return t;
	    }, projectId);
	}

	public List<Task> findUnassignedBasicByProject(int projectId) {
	    final String sql = """
	        SELECT t.task_id, t.project_id, t.title, t.description, t.priority, t.status, t.type
	        FROM tasks t
	        LEFT JOIN task_assignees ta ON ta.task_id = t.task_id
	        WHERE t.project_id = ?
	          AND LOWER(t.status) <> 'done'
	        GROUP BY t.task_id
	        HAVING COUNT(ta.user_id) = 0
	        ORDER BY t.created_at ASC
	    """;
	    return jdbc.query(sql, (rs, i) -> {
	        Task t = new Task();
	        t.setTaskId(rs.getInt("task_id"));
	        t.setTitle(rs.getString("title"));
	        t.setDescription(rs.getString("description"));
	        t.setPriority(rs.getString("priority"));
	        t.setStatus(rs.getString("status"));
	        t.setType(rs.getString("type"));
	        
			/* t.setCreatedAt(rs.getObject("created_at", LocalDateTime.class)); */
			/* t.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class)); */
	        return t;
	    }, projectId);
	}



	// ===== SUMMARY QUERIES =====

	public MetricDto getMetricsByProject(int projectId) {
	    String sql = """
	        SELECT
	          COUNT(*) AS total,
	          SUM(status = 'Done') AS completed,
	          SUM(
	              due_date IS NOT NULL
	              AND due_date <= CURDATE() + INTERVAL 7 DAY
	              AND status <> 'Done'
	          ) AS due_soon,
	          SUM(DATE(created_at) = CURDATE()) AS created_today,
	          SUM(
	              DATE(updated_at) = CURDATE()
	              AND (updated_at IS NOT NULL)
	              AND (updated_at <> created_at)
	          ) AS updated_today
	        FROM tasks
	        WHERE project_id = ?
	    """;

	    return jdbc.queryForObject(sql, (rs, i) -> {
	        MetricDto m = new MetricDto();
	        m.setTotal(rs.getInt("total"));
	        m.setCompleted(rs.getInt("completed"));
	        m.setDueSoon(rs.getInt("due_soon"));
	        m.setCreatedToday(rs.getInt("created_today"));
	        m.setUpdatedToday(rs.getInt("updated_today"));
	        return m;
	    }, projectId);
	}

	public List<CountDto> countByStatus(int projectId) {
	    String sql = """
	        SELECT status AS label, COUNT(*) AS cnt
	        FROM tasks
	        WHERE project_id = ?
	        GROUP BY status
	        ORDER BY cnt DESC
	    """;
	    return jdbc.query(sql, (rs, i) ->
	        new CountDto(rs.getString("label"), rs.getInt("cnt"))
	    , projectId);
	}
	public List<LabelCountDto> countTasksByStatus() {
        String sql = """
            SELECT status, COUNT(*) AS cnt
            FROM tasks
            GROUP BY status
            ORDER BY status
        """;
        return jdbc.query(sql, (rs, i) ->
                new com.example.projectmanageweb.dto.LabelCountDto(
                        rs.getString("status"),
                        rs.getLong("cnt")
                )
        );
    }

	public List<CountDto> countByPriority(int projectId) {
	    String sql = """
	        SELECT priority AS label, COUNT(*) AS cnt
	        FROM tasks
	        WHERE project_id = ?
	        GROUP BY priority
	        ORDER BY cnt DESC
	    """;
	    return jdbc.query(sql, (rs, i) ->
	        new CountDto(rs.getString("label"), rs.getInt("cnt"))
	    , projectId);
	}

	public List<CountDto> countByType(int projectId) {
	    String sql = """
	        SELECT COALESCE(type, task_type, 'Task') AS label, COUNT(*) AS cnt
	        FROM tasks
	        WHERE project_id = ?
	        GROUP BY COALESCE(type, task_type, 'Task')
	        ORDER BY cnt DESC
	    """;
	    return jdbc.query(sql, (rs, i) ->
	        new CountDto(rs.getString("label"), rs.getInt("cnt"))
	    , projectId);
	}

	public List<RecentActivityDto> findRecentActivities(int projectId, int limit) {
	    String sql = """
	        SELECT *
	        FROM (
	            SELECT
	                t.task_id,
	                t.title,
	                t.status,
	                t.created_at AS time,
	                'created' AS action,
	                u.full_name AS actor
	            FROM tasks t
	            LEFT JOIN users u ON u.user_id = t.created_by
	            WHERE t.project_id = ?

	            UNION ALL

	            SELECT
	                t.task_id,
	                t.title,
	                t.status,
	                t.updated_at AS time,
	                'updated' AS action,
	                u.full_name AS actor
	            FROM tasks t
	            LEFT JOIN users u ON u.user_id = t.created_by
	            WHERE t.project_id = ?
	              AND t.updated_at IS NOT NULL
	              AND t.updated_at <> t.created_at
	        ) x
	        ORDER BY time DESC
	        LIMIT ?
	    """;

	    return jdbc.query(sql, (rs, i) -> {
	        RecentActivityDto a = new RecentActivityDto();
	        a.setTaskId(rs.getInt("task_id"));
	        a.setTitle(rs.getString("title"));
	        a.setStatus(rs.getString("status"));
	        a.setAction(rs.getString("action"));
	        a.setActor(rs.getString("actor"));
	        a.setTime(rs.getTimestamp("time").toLocalDateTime());
	        return a;
	    }, projectId, projectId, limit);
	}
	
	public void deleteById(int taskId) {
	    String sql = "DELETE FROM tasks WHERE task_id = ?";
	    jdbc.update(sql, taskId);
	}


	public List<MemberTaskItem> findByProjectAndAssignee(int projectId, int userId) {
        final String sql = """
            SELECT t.task_id,
                   t.title,
                   t.status,
                   t.priority,
                   t.type,
                   t.due_date
            FROM tasks t
            JOIN task_assignees ta
                 ON ta.task_id = t.task_id
            WHERE t.project_id = ?
              AND ta.user_id = ?
            ORDER BY t.due_date IS NULL, t.due_date, t.created_at
            """;

        return jdbc.query(sql, memberTaskItemRowMapper, projectId, userId);
    }
	
    // Đếm số task user này đang được assign (status != Done)
    public int countOpenTasksOfUser(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM tasks t
            JOIN task_assignees ta ON ta.task_id = t.task_id
            WHERE ta.user_id = ?
              AND LOWER(t.status) <> 'done'
        """;
        Integer c = jdbc.queryForObject(sql, Integer.class, userId);
        return c == null ? 0 : c;
    }

    // Activity của 1 user trên tất cả project
    public List<RecentActivityDto> findRecentActivitiesByUser(int userId, int limit) {
        String sql = """
            SELECT *
            FROM (
                -- task do user này tạo
                SELECT
                    t.task_id,
                    t.title,
                    t.status,
                    t.created_at AS time,
                    'created' AS action,
                    u.full_name AS actor
                FROM tasks t
                JOIN users u ON u.user_id = t.created_by
                WHERE t.created_by = ?

                UNION ALL

                -- task user này được assign và có update
                SELECT
                    t.task_id,
                    t.title,
                    t.status,
                    t.updated_at AS time,
                    'updated' AS action,
                    u.full_name AS actor
                FROM tasks t
                JOIN task_assignees ta ON ta.task_id = t.task_id
                JOIN users u ON u.user_id = ta.user_id
                WHERE ta.user_id = ?
                  AND t.updated_at IS NOT NULL
                  AND t.updated_at <> t.created_at
            ) x
            ORDER BY time DESC
            LIMIT ?
        """;

        return jdbc.query(sql, (rs, i) -> {
            RecentActivityDto a = new RecentActivityDto();
            a.setTaskId(rs.getInt("task_id"));
            a.setTitle(rs.getString("title"));
            a.setStatus(rs.getString("status"));
            a.setAction(rs.getString("action"));
            a.setActor(rs.getString("actor"));
            a.setTime(rs.getTimestamp("time").toLocalDateTime());
            return a;
        }, userId, userId, limit);
    }

    public TaskBasic findBasicById(int taskId) {
        String sql = """
            SELECT task_id, title, status, type
            FROM tasks
            WHERE task_id = ?
        """;

        List<TaskBasic> list = jdbc.query(sql, (rs, i) -> {
            TaskBasic t = new TaskBasic();
            t.setTaskId(rs.getInt("task_id"));
            t.setTitle(rs.getString("title"));
            t.setStatus(rs.getString("status"));
            t.setType(rs.getString("type"));
            return t;
        }, taskId);

        return list.isEmpty() ? null : list.get(0);
    }
    
    public int countAllTasks() {
        String sql = "SELECT COUNT(*) FROM tasks";
        Integer n = jdbc.queryForObject(sql, Integer.class);
        return n != null ? n : 0;
    }



}
