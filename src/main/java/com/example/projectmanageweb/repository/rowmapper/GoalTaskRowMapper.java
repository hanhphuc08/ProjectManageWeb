package com.example.projectmanageweb.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.dto.GoalTaskDto;

public class GoalTaskRowMapper implements RowMapper<GoalTaskDto> {
	@Override
    public GoalTaskDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GoalTaskDto dto = new GoalTaskDto();
        dto.setTaskId(rs.getInt("task_id"));
        dto.setTitle(rs.getString("title"));
        dto.setStatus(rs.getString("status"));
        if (rs.getDate("due_date") != null) {
            dto.setDueDate(rs.getDate("due_date").toLocalDate());
        }
        return dto;
    }

}
