package com.example.projectmanageweb.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.projectmanageweb.dto.LabelCountDto;
import com.example.projectmanageweb.dto.ProjectListItem;
import com.example.projectmanageweb.dto.RecentProjectDto;
import com.example.projectmanageweb.repository.rowmapper.ProjectListItemRowMapper;

@Repository
public class ProjectsRepository {

    private final JdbcTemplate jdbc;
    private final ProjectListItemRowMapper listMapper = new ProjectListItemRowMapper();

    public ProjectsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    
    public String findProjectNameById(int projectId) {
        String sql = "SELECT project_name FROM projects WHERE project_id = ?";
        return jdbc.queryForObject(sql, String.class, projectId);
    }



    /* ============================================
       CREATE PROJECT
       ============================================ */
    public int createProject(String name, Integer typeId, String desc,
                             LocalDate start, LocalDate end, Integer createdBy) {

        final String sql = """
            INSERT INTO projects
            (project_name, type_id, description, start_date, end_date,
             status, created_by, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, 'Planned', ?, NOW(), NOW())
        """;

        KeyHolder kh = new GeneratedKeyHolder();

        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            if (typeId != null) ps.setInt(2, typeId); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, desc);
            if (start != null) ps.setDate(4, Date.valueOf(start)); else ps.setNull(4, Types.DATE);
            if (end != null) ps.setDate(5, Date.valueOf(end)); else ps.setNull(5, Types.DATE);
            ps.setInt(6, createdBy);

            return ps;
        }, kh);

        return kh.getKey().intValue();
    }



    /* ============================================
       BASE SELECT DÙNG CHUNG CHO LIST & DETAIL
       ============================================ */
    private static final String BASE_SELECT = """
        SELECT
            p.project_id,
            p.project_name,
            p.description,
            p.status,
            p.start_date,
            p.end_date,

            -- Creator / Project leader
            COALESCE(u.full_name, '-') AS leader_name,

            -- Project type
            pt.type_name,
            pt.type_code,

            -- PM assigned (nếu có)
            pm.full_name AS pm_name,

            -- Member count
            COALESCE(mem.member_count, 0) AS member_count

        FROM projects p
        LEFT JOIN users u
            ON u.user_id = p.created_by

        LEFT JOIN project_types pt
            ON pt.type_id = p.type_id

        -- PM info
        LEFT JOIN (
            SELECT m.project_id, us.full_name
            FROM project_members m
            JOIN users us ON us.user_id = m.user_id
            WHERE m.role_in_project = 'PM'
        ) pm
            ON pm.project_id = p.project_id

        -- Member counter
        LEFT JOIN (
            SELECT project_id, COUNT(*) AS member_count
            FROM project_members
            GROUP BY project_id
        ) mem
            ON mem.project_id = p.project_id
    """;



    /* ============================================
       LIST FOR USER
       ============================================ */
    public List<ProjectListItem> listForUser(int userId, String q, boolean admin) {
        boolean hasQ = (q != null && !q.isBlank());

        if (admin) {
            String sql = BASE_SELECT + """
                WHERE (? IS NULL OR p.project_name LIKE CONCAT('%', ?, '%'))
                ORDER BY p.updated_at DESC, p.project_id DESC
            """;

            return jdbc.query(
                    sql,
                    listMapper,
                    hasQ ? q : null,
                    hasQ ? q : null
            );
        }

        // User thường: chỉ xem project mình là member
        String sql = BASE_SELECT + """
            JOIN project_members m_user
                ON m_user.project_id = p.project_id
               AND m_user.user_id = ?

            WHERE (? IS NULL OR p.project_name LIKE CONCAT('%', ?, '%'))
            ORDER BY p.updated_at DESC, p.project_id DESC
        """;

        return jdbc.query(
                sql,
                listMapper,
                userId,
                hasQ ? q : null,
                hasQ ? q : null
        );
    }



    /* ============================================
       FIND PROJECT DETAIL
       ============================================ */
    public ProjectListItem findProjectDetail(int projectId, int userId, boolean admin) {

        List<ProjectListItem> result;

        if (admin) {
            String sql = BASE_SELECT + """
                WHERE p.project_id = ?
            """;

            result = jdbc.query(sql, listMapper, projectId);

        } else {
            String sql = BASE_SELECT + """
                JOIN project_members m_user
                    ON m_user.project_id = p.project_id
                   AND m_user.user_id = ?

                WHERE p.project_id = ?
            """;

            result = jdbc.query(sql, listMapper, userId, projectId);
        }

        return result.isEmpty() ? null : result.get(0);
    }
    
    public int countAllProjects() {
        String sql = "SELECT COUNT(*) FROM projects";
        Integer n = jdbc.queryForObject(sql, Integer.class);
        return n != null ? n : 0;
    }

    public List<LabelCountDto> countProjectsByStatus() {
        String sql = """
            SELECT status, COUNT(*) AS cnt
            FROM projects
            GROUP BY status
            ORDER BY status
        """;
        return jdbc.query(sql, (rs, i) ->
                new com.example.projectmanageweb.dto.LabelCountDto(
                        rs.getString("status"),
                        rs.getLong("cnt")
                )
        );
    }

    public List<RecentProjectDto> findRecentProjects(int limit) {
        String sql = """
            SELECT p.project_id,
                   p.project_name,
                   p.status,
                   p.start_date,
                   p.end_date,
                   pt.type_name,
                   u.full_name AS creator_name
            FROM projects p
            LEFT JOIN project_types pt ON pt.type_id = p.type_id
            LEFT JOIN users u ON u.user_id = p.created_by
            ORDER BY p.updated_at DESC, p.project_id DESC
            LIMIT ?
        """;

        return jdbc.query(sql, (rs, i) -> {
            RecentProjectDto dto = new RecentProjectDto();
            dto.setProjectId(rs.getInt("project_id"));
            dto.setName(rs.getString("project_name"));
            dto.setStatus(rs.getString("status"));
            dto.setTypeName(rs.getString("type_name"));
            dto.setCreatedByName(rs.getString("creator_name"));
            java.sql.Date sd = rs.getDate("start_date");
            java.sql.Date ed = rs.getDate("end_date");
            dto.setStartDate(sd != null ? sd.toLocalDate() : null);
            dto.setEndDate(ed != null ? ed.toLocalDate() : null);
            return dto;
        }, limit);
    }

    
}
