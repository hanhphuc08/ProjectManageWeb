package com.example.projectmanageweb.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.projectmanageweb.dto.board.BoardMemberItem;

public class BoardMemberItemRowMapper implements RowMapper<BoardMemberItem>{

	@Override
	public BoardMemberItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardMemberItem m = new BoardMemberItem();
        m.setProjectMemberId(rs.getInt("project_member_id"));
        m.setUserId(rs.getInt("user_id"));
        m.setFullName(rs.getString("full_name"));
        m.setRoleInProject(rs.getString("role_in_project"));
        m.setAvatarUrl(rs.getString("avatar_url"));
        return m;
	}

}
