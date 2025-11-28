package com.example.projectmanageweb.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.ProjectMemberAddForm;
import com.example.projectmanageweb.model.ProjectMember;
import com.example.projectmanageweb.model.User;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.ProjectsRepository;
import com.example.projectmanageweb.repository.UserRepository;

@Service
public class ProjectMembersService {
	private final ProjectMembersRepository membersRepo;
    private final NotificationEmailService notificationEmailService;
    private final UserRepository userRepository;
    private final ProjectsRepository projectsRepo;


	public ProjectMembersService(ProjectMembersRepository membersRepo,
			NotificationEmailService notificationEmailService, UserRepository userRepository,
			ProjectsRepository projectsRepo) {
		super();
		this.membersRepo = membersRepo;
		this.notificationEmailService = notificationEmailService;
		this.userRepository = userRepository;
		this.projectsRepo = projectsRepo;
	}
	public void addMemberWithSkills(int projectId, int currentUserId, ProjectMemberAddForm form) {

		if (!membersRepo.isPmOfProject(projectId, currentUserId)) {
			throw new AccessDeniedException("Chỉ PM mới được thêm thành viên vào dự án.");
		}

		if (membersRepo.existsByProjectAndUser(projectId, form.getUserId())) {
			throw new IllegalStateException("User này đã nằm trong dự án rồi.");
		}

		int pmId = membersRepo.insertMember(projectId, form.getUserId(), "Member", form.getProjectRoleId(),
				form.getAllocationPct(), form.getAvailability());

		String skillsStr = form.getSkills();
		if (skillsStr != null && !skillsStr.isBlank()) {
			String[] parts = skillsStr.split(",");
			for (String raw : parts) {
				if (raw == null)
					continue;
				String name = raw.trim();
				if (name.isEmpty())
					continue;
				membersRepo.addSkillForMember(pmId, name);
			}
		}

		//  Gửi email báo được thêm vào dự án
		User newMember = userRepository.findById(form.getUserId());
		User currentUser = userRepository.findById(currentUserId);

		if (newMember != null && newMember.getEmail() != null) {
			String projectName = projectsRepo.findProjectNameById(projectId);
			String invitedByName = currentUser != null ? currentUser.getFullName() : "PM";

			notificationEmailService.sendProjectMemberAdded(newMember.getEmail(), newMember.getFullName(), projectName,
					invitedByName);
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
