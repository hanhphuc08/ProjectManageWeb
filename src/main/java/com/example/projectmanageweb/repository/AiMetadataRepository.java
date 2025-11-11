package com.example.projectmanageweb.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.ProjectSummary;
import com.example.projectmanageweb.dto.WbsNodeSummary;

@Repository
public class AiMetadataRepository {
	private final JdbcTemplate jdbc;

    public AiMetadataRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<ProjectSummary> projectSummaryMapper = (rs, rowNum) -> {
        ProjectSummary p = new ProjectSummary();
        p.setProjectId(rs.getInt("project_id"));
        p.setProjectName(rs.getString("project_name"));
        p.setDescription(rs.getString("description"));
        p.setProjectTypeName(rs.getString("type_name"));
        return p;
    };

    public ProjectSummary findProjectSummary(Integer projectId) {
        String sql = """
                SELECT p.project_id,
                       p.project_name,
                       p.description,
                       pt.type_name
                FROM projects p
                LEFT JOIN project_types pt ON pt.type_id = p.type_id
                WHERE p.project_id = ?
                """;
        return jdbc.queryForObject(sql, projectSummaryMapper, projectId);
    }

}
