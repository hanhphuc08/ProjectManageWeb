package com.example.projectmanageweb.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.dto.GoalViewDto;

public class GoalViewRowMapper implements RowMapper<GoalViewDto> {
	@Override
    public GoalViewDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GoalViewDto g = new GoalViewDto();
        g.setGoalId(rs.getInt("goal_id"));
        g.setTitle(rs.getString("title"));
        g.setDescription(rs.getString("description"));
        g.setStatus(rs.getString("status"));
        g.setTargetDate(rs.getDate("target_date") != null
                ? rs.getDate("target_date").toLocalDate()
                : null);
        g.setOwnerName(rs.getString("owner_name"));
        g.setTotalTasks(rs.getInt("total_tasks"));
        g.setDoneTasks(rs.getInt("done_tasks"));

        int total = g.getTotalTasks();
        int done = g.getDoneTasks();
        int pct = (total > 0) ? (int) Math.round(done * 100.0 / total) : 0;
        g.setProgressPercent(pct);

        return g;
    }

	
}
