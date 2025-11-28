package com.example.projectmanageweb.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.dto.MemberTaskItem;

public class MemberTaskItemRowMapper implements RowMapper<MemberTaskItem> {
	
	@Override
    public MemberTaskItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemberTaskItem t = new MemberTaskItem();
        t.setTaskId(rs.getInt("task_id"));
        t.setTitle(rs.getString("title"));
        t.setStatus(rs.getString("status"));
        t.setPriority(rs.getString("priority"));
        t.setType(rs.getString("type"));
        if (rs.getDate("due_date") != null) {
            t.setDueDate(rs.getDate("due_date").toLocalDate());
        }
        return t;
    }

}
