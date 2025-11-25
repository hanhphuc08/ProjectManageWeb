package com.example.projectmanageweb.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.calendar.CalendarTaskDto;



@Repository
public class CalendarRepository {
	private final JdbcTemplate jdbc;

    public CalendarRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<CalendarTaskDto> getTasksByMonth(int projectId, YearMonth ym) {

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        String sql = """
            SELECT t.task_id, t.title, t.status, t.type,
                   t.due_date,
                   u.full_name AS assignee
            FROM tasks t
            LEFT JOIN task_assignees ta ON ta.task_id = t.task_id
            LEFT JOIN users u ON u.user_id = ta.user_id
            WHERE t.project_id = ?
              AND t.due_date BETWEEN ? AND ?
            ORDER BY t.due_date ASC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            CalendarTaskDto dto = new CalendarTaskDto();
            dto.setTaskId(rs.getInt("task_id"));
            dto.setTitle(rs.getString("title"));
            dto.setStatus(rs.getString("status"));
            dto.setType(rs.getString("type"));
            dto.setDueDate(rs.getDate("due_date").toLocalDate());
            dto.setAssigneeName(rs.getString("assignee"));
            return dto;
        }, projectId, start, end);
    }

}
