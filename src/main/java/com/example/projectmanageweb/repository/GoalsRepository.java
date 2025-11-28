package com.example.projectmanageweb.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.GoalStats;
import com.example.projectmanageweb.dto.GoalTaskDto;
import com.example.projectmanageweb.dto.GoalViewDto;
import com.example.projectmanageweb.model.Task;
import com.example.projectmanageweb.repository.rowmapper.GoalTaskRowMapper;
import com.example.projectmanageweb.repository.rowmapper.GoalViewRowMapper;
import com.example.projectmanageweb.repository.rowmapper.TaskRowMapper;

@Repository
public class GoalsRepository {
	
	private final JdbcTemplate jdbc;
    private final GoalViewRowMapper viewMapper = new GoalViewRowMapper();
    private final TaskRowMapper taskRowMapper = new TaskRowMapper();
    private final GoalTaskRowMapper goalTaskMapper = new GoalTaskRowMapper();

    public GoalsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
 // Danh sách goals + progress theo project
    public List<GoalViewDto> listByProject(int projectId) {
        final String sql = """
            SELECT g.goal_id, g.title, g.description, g.status, g.target_date,
                   COALESCE(u.full_name, '-') AS owner_name,
                   COUNT(gt.task_id) AS total_tasks,
                   SUM(CASE WHEN t.status = 'Done' THEN 1 ELSE 0 END) AS done_tasks
            FROM goals g
            LEFT JOIN users u       ON u.user_id = g.owner_id
            LEFT JOIN goal_tasks gt ON gt.goal_id = g.goal_id
            LEFT JOIN tasks t       ON t.task_id = gt.task_id
            WHERE g.project_id = ?
            GROUP BY g.goal_id, g.title, g.description, g.status, g.target_date, owner_name
            ORDER BY g.target_date IS NULL, g.target_date
            """;
        return jdbc.query(sql, viewMapper, projectId);
    }

    // Tạo goal
    public int createGoal(int projectId, String title, String desc,
                          LocalDate targetDate, Integer ownerId) {

        final String sql = """
            INSERT INTO goals (project_id, title, description, status, target_date, owner_id)
            VALUES (?, ?, ?, 'NOT_STARTED', ?, ?)
            """;

        KeyHolder kh = new GeneratedKeyHolder();

        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, projectId);
            ps.setString(2, title);
            ps.setString(3, desc);
            if (targetDate != null) {
                ps.setDate(4, java.sql.Date.valueOf(targetDate));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            if (ownerId != null) {
                ps.setInt(5, ownerId);
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            return ps;
        }, kh);

        return kh.getKey().intValue();
    }

    // Gán task vào goal
    public void addTasksToGoal(int goalId, List<Integer> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) return;

        final String sql = "INSERT IGNORE INTO goal_tasks (goal_id, task_id) VALUES (?, ?)";

        jdbc.batchUpdate(sql, taskIds, taskIds.size(), (ps, taskId) -> {
            ps.setInt(1, goalId);
            ps.setInt(2, taskId);
        });
    }
    
 // Các task trong project CHƯA gán vào goal này
    public List<Task> findAvailableTasks(int projectId, int goalId) {
        final String sql = """
            SELECT t.*
            FROM tasks t
            LEFT JOIN goal_tasks gt
              ON gt.task_id = t.task_id
             AND gt.goal_id = ?
            WHERE t.project_id = ?
              AND gt.task_id IS NULL
            ORDER BY t.status, t.due_date, t.task_id
            """;

        return jdbc.query(sql, taskRowMapper, goalId, projectId);
    }
    
 // Các task ĐÃ gán vào 1 goal
    public List<GoalTaskDto> listTasksOfGoal(int projectId, int goalId) {
        final String sql = """
            SELECT t.task_id, t.title, t.status, t.due_date
            FROM goal_tasks gt
            JOIN tasks t ON t.task_id = gt.task_id
            WHERE gt.goal_id = ? AND t.project_id = ?
            ORDER BY t.status, t.due_date, t.task_id
            """;

        return jdbc.query(sql, goalTaskMapper, goalId, projectId);
    }
    
    public List<Integer> findGoalIdsByTask(int taskId) {
        final String sql = "SELECT goal_id FROM goal_tasks WHERE task_id = ?";
        return jdbc.query(sql, (rs, rowNum) -> rs.getInt("goal_id"), taskId);
    }

    public void recomputeGoalStatus(int goalId) {
        // Lấy total & done cho goal này
        final String sql = """
            SELECT COUNT(*) AS total_tasks,
                   SUM(CASE WHEN t.status = 'Done' THEN 1 ELSE 0 END) AS done_tasks
            FROM goal_tasks gt
            JOIN tasks t ON t.task_id = gt.task_id
            WHERE gt.goal_id = ?
            """;

        Integer total = 0;
        Integer done  = 0;

        var res = jdbc.queryForMap(sql, goalId);
        if (res != null) {
            total = ((Number) res.get("total_tasks")).intValue();
            Number d = (Number) res.get("done_tasks");
            done = (d == null ? 0 : d.intValue());
        }

        String newStatus;
        if (total == 0) {
            newStatus = "NOT_STARTED";
        } else if (done == 0) {
            newStatus = "NOT_STARTED";
        } else if (done.equals(total)) {
            newStatus = "DONE";
        } else {
            newStatus = "IN_PROGRESS";
        }

        jdbc.update("UPDATE goals SET status = ? WHERE goal_id = ?", newStatus, goalId);
    }
    
 // Gỡ 1 task khỏi 1 goal
    public void removeTaskFromGoal(int goalId, int taskId) {
        final String sql = "DELETE FROM goal_tasks WHERE goal_id = ? AND task_id = ?";
        jdbc.update(sql, goalId, taskId);
    }

    // Gỡ nhiều task 1 lúc
    public void removeTasksFromGoal(int goalId, List<Integer> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) return;

        final String sql = "DELETE FROM goal_tasks WHERE goal_id = ? AND task_id = ?";
        jdbc.batchUpdate(sql, taskIds, taskIds.size(), (ps, taskId) -> {
            ps.setInt(1, goalId);
            ps.setInt(2, taskId);
        });
    }
    
    
    // Thống kê goal cho 1 user (owner_id)
    public GoalStats getGoalStatsForUser(int userId) {
        GoalStats stats = new GoalStats();

        // 1) Active & completed
        String sqlCount = """
            SELECT
              SUM(CASE WHEN status = 'DONE' THEN 1 ELSE 0 END) AS completed,
              SUM(CASE WHEN status <> 'DONE' THEN 1 ELSE 0 END) AS active
            FROM goals
            WHERE owner_id = ?
        """;

        var map = jdbc.queryForMap(sqlCount, userId);
        if (map != null) {
            Number cCompleted = (Number) map.get("completed");
            Number cActive    = (Number) map.get("active");
            stats.setCompletedGoals(cCompleted == null ? 0 : cCompleted.intValue());
            stats.setActiveGoals(cActive == null ? 0 : cActive.intValue());
        }

        // 2) Goal sắp tới (theo target_date nhỏ nhất, chưa DONE)
        String sqlNext = """
            SELECT title, target_date
            FROM goals
            WHERE owner_id = ?
              AND status <> 'DONE'
              AND target_date IS NOT NULL
            ORDER BY target_date ASC
            LIMIT 1
        """;

        var list = jdbc.query(sqlNext, (rs, i) -> {
            GoalStats tmp = new GoalStats();
            tmp.setNextGoalTitle(rs.getString("title"));
            java.sql.Date d = rs.getDate("target_date");
            if (d != null) {
                tmp.setNextGoalDate(
                    d.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
            }
            return tmp;
        }, userId);

        if (!list.isEmpty()) {
            GoalStats g = list.get(0);
            stats.setNextGoalTitle(g.getNextGoalTitle());
            stats.setNextGoalDate(g.getNextGoalDate());
        }

        return stats;
    }


}
