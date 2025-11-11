package com.example.projectmanageweb.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.model.Task;

public class TaskRowMapper implements RowMapper<Task> {

	@Override
	public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
		Task t = new Task();
        t.setTaskId(rs.getInt("task_id"));
        t.setProjectId(rs.getInt("project_id"));

        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));

        t.setStatus(rs.getString("status"));     // 'To Do','In Progress',...
        t.setPriority(rs.getString("priority")); // 'Low','Medium','High'

        LocalDate startDate = rs.getObject("start_date", LocalDate.class);
        LocalDate dueDate = rs.getObject("due_date", LocalDate.class);
        t.setStartDate(startDate);
        t.setDueDate(dueDate);

        // created_by có thể null → dùng getObject
        Integer createdBy = (Integer) rs.getObject("created_by");
        t.setCreatedBy(createdBy);

        LocalDateTime createdAt = rs.getObject("created_at", LocalDateTime.class);
        LocalDateTime updatedAt = rs.getObject("updated_at", LocalDateTime.class);
        t.setCreatedAt(createdAt);
        t.setUpdatedAt(updatedAt);

        // Nếu DB có cột type thì map, không thì bỏ
        try {
            String type = rs.getString("type");
            t.setType(type);
        } catch (SQLException ex) {
       
        }

        return t;
	}
	

}
