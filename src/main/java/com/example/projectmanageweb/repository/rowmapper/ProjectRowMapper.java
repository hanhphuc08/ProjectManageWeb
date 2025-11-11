package com.example.projectmanageweb.repository.rowmapper;

import java.security.Timestamp;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.model.Project;

public class ProjectRowMapper implements RowMapper<Project> {

	@Override
	public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Project p = new Project();
		p.setProjectId(rs.getInt("project_id"));
		p.setProjectName(rs.getString("project_name"));
		p.setDescription(rs.getString("project_description"));
		
		Date sd = rs.getDate("start_date");
		if(sd != null) {
			p.setStartDate(sd.toLocalDate());
		}
		Date ed = rs.getDate("end_date");
		if(ed != null) {
			p.setEndDate(sd.toLocalDate());
		}
		
		p.setStatus(rs.getString("status"));
		Object cb = rs.getObject("create_by");
		p.setCreatedBy(cb != null ? rs.getInt("create_by") : null);
		
		java.sql.Timestamp cAt = rs.getTimestamp("created_at");
        if (cAt != null) p.setCreatedAt(cAt.toInstant());
        java.sql.Timestamp uAt = rs.getTimestamp("updated_at");
        if (uAt != null) p.setUpdatedAt(uAt.toInstant());
        
        
        return p;
	}
	

}
