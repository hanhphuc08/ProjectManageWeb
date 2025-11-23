package com.example.projectmanageweb.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import com.example.projectmanageweb.dto.board.BoardTaskItem;

public class BoardTaskItemRowMapper implements RowMapper<BoardTaskItem> {

    @Override
    public BoardTaskItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        BoardTaskItem item = new BoardTaskItem();
        item.setTaskId(rs.getInt("task_id"));
        item.setProjectId(rs.getInt("project_id"));
        item.setSummary(rs.getString("title"));
        item.setType(rs.getString("type"));
        item.setStatus(rs.getString("status"));
        item.setPriority(rs.getString("priority"));

        item.setDueDate(rs.getObject("due_date", LocalDate.class));
        item.setStartDate(rs.getObject("start_date", LocalDate.class));
        item.setCreatedAt(rs.getObject("created_at", LocalDate.class));

        try {
            item.setEndDate(rs.getObject("end_date", LocalDate.class));
        } catch (SQLException ignore) {}

        // ===== assignees (from GROUP_CONCAT) =====
        String assigneesStr = null;
        String assigneeIdsStr = null;
        try {
            assigneesStr = rs.getString("assignees");
            assigneeIdsStr = rs.getString("assignee_ids");
        } catch (SQLException ignore) {}

        item.setAssignees(splitToStringList(assigneesStr));
        item.setAssigneeIds(splitToIntList(assigneeIdsStr));

        return item;
    }

    private List<String> splitToStringList(String s){
        if(s == null || s.isBlank()) return Collections.emptyList();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());
    }

    private List<Integer> splitToIntList(String s){
        if(s == null || s.isBlank()) return Collections.emptyList();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
