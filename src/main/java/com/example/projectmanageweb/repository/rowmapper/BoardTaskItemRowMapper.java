package com.example.projectmanageweb.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.dto.board.BoardTaskItem;

public class BoardTaskItemRowMapper implements RowMapper<BoardTaskItem> {

	@Override
	public BoardTaskItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardTaskItem item = new BoardTaskItem();
        item.setTaskId(rs.getInt("task_id"));
        item.setSummary(rs.getString("title"));  // map title -> summary
        item.setType(rs.getString("type"));      // Task/Bug/Feature
        item.setStatus(rs.getString("status"));  // 'To Do','In Progress',...
        item.setPriority(rs.getString("priority"));

        LocalDate dueDate = rs.getObject("due_date", LocalDate.class);
        item.setDueDate(dueDate);

        return item;
	}

}
