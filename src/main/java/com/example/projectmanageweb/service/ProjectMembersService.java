package com.example.projectmanageweb.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.ProjectMemberAddForm;
import com.example.projectmanageweb.model.ProjectMember;
import com.example.projectmanageweb.repository.ProjectMembersRepository;

@Service
public class ProjectMembersService {
	private final ProjectMembersRepository membersRepo;

	public ProjectMembersService(ProjectMembersRepository membersRepo) {
		this.membersRepo = membersRepo;
	}

	public void addMemberWithSkills(int projectId, int currentUserId,
			ProjectMemberAddForm form) {

		// 1. Chỉ PM của project mới được thêm member
		if (!membersRepo.isPmOfProject(projectId, currentUserId)) {
			throw new AccessDeniedException("Chỉ PM mới được thêm thành viên vào dự án.");
		}

		// 2. Không cho thêm trùng user
		if (membersRepo.existsByProjectAndUser(projectId, form.getUserId())) {
			throw new IllegalStateException("User này đã nằm trong dự án rồi.");
		}

		// 3. Insert project_members
		 int pmId = membersRepo.insertMember(
		            projectId,
		            form.getUserId(),
		            "Member",                     
		            form.getProjectRoleId(),        
		            form.getAllocationPct(),
		            form.getAvailability()
		    );

		// 4. Insert project_member_skills
		 String skillsStr = form.getSkills(); 
		    if (skillsStr != null && !skillsStr.isBlank()) {
		        String[] parts = skillsStr.split(","); 

		        for (String raw : parts) {
		            if (raw == null) continue;
		            String name = raw.trim();
		            if (name.isEmpty()) continue;

		            // nếu muốn tránh trùng kỹ năng:
		            // có thể lowercase rồi dùng Set, nhưng tạm thời chỉ add
		            membersRepo.addSkillForMember(pmId, name);
		        }
		    }
	}
	public void removeMember(int projectId, int currentUserId, int projectMemberId) {

        // 1. chỉ PM được quản lý team
        if (!membersRepo.isPmOfProject(projectId, currentUserId)) {
            throw new AccessDeniedException("Chỉ PM mới được quản lý thành viên dự án.");
        }

        // 2. lấy member cần xóa
        ProjectMember member = membersRepo.findById(projectMemberId);
        if (member == null || member.getProjectId() != projectId) {
            throw new IllegalArgumentException("Thành viên không tồn tại trong dự án.");
        }

        // 3. không cho xóa PM cuối cùng
        boolean targetIsPm = "PM".equalsIgnoreCase(member.getRole());
        if (targetIsPm) {
            int pmCount = membersRepo.countPmOfProject(projectId);
            if (pmCount <= 1) {
                throw new IllegalStateException("Không thể xóa PM cuối cùng của dự án.");
            }
        }

        // 4. thực hiện xóa
        membersRepo.deleteMember(projectMemberId);

        // (optional) nếu muốn xóa cả assignment ở task_assignees thì code thêm
        // chứ DB hiện tại không cascade theo project_members
    }
	public void requirePm(int projectId, int userId) {
        if (!membersRepo.isPmOfProject(projectId, userId)) {
            throw new AccessDeniedException("Only PM can do this action.");
        }
    }
	public boolean isPm(int projectId, int userId) {
        return membersRepo.isPmOfProject(projectId, userId);
    }
	

}
