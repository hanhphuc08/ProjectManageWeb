package com.example.projectmanageweb.repository.rowmapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.dto.ProjectListItem;

public class ProjectListItemRowMapper implements RowMapper<ProjectListItem> {
	  @Override
	  public ProjectListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		  ProjectListItem item = new ProjectListItem();

	        item.setProjectId(rs.getInt("project_id"));
	        item.setProjectName(rs.getString("project_name"));
	        item.setDescription(rs.getString("description"));
	        item.setStatus(rs.getString("status"));

	        Date start = rs.getDate("start_date");
	        if (start != null) {
	            item.setStartDate(start.toLocalDate());
	        }

	        Date end = rs.getDate("end_date");
	        if (end != null) {
	            item.setEndDate(end.toLocalDate());
	        }

	        
	        item.setLeaderName(rs.getString("leader_name"));

	        // Type
	        item.setTypeName(rs.getString("type_name"));
	        item.setTypeCode(rs.getString("type_code"));

	        // Member count
	        int memberCount = rs.getInt("member_count");
	        if (rs.wasNull()) {
	            memberCount = 0;
	        }
	        item.setMemberCount(memberCount);

	        return item;
	    }
	}