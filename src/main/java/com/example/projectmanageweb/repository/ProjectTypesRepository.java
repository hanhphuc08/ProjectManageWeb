package com.example.projectmanageweb.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.model.ProjectType;

@Repository
public class ProjectTypesRepository {
	private final JdbcTemplate jdbc;
	
	
	public ProjectTypesRepository(JdbcTemplate jdbc) {
		super();
		this.jdbc = jdbc;
	}

	private final RowMapper<ProjectType> mapper = (rs, i) -> {
	    var t = new ProjectType();
	    t.setTypeId(rs.getInt("type_id"));
	    t.setTypeCode(rs.getString("type_code"));
	    t.setTypeName(rs.getString("type_name"));
	    return t;
	  };
	  
	  public List<ProjectType> findAll() {
		    return jdbc.query("SELECT type_id,type_code,type_name FROM project_types ORDER BY type_name", mapper);
		  }
	  

}
