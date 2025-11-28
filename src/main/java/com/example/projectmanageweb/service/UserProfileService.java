package com.example.projectmanageweb.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.projectmanageweb.dto.ActiveProjectItem;
import com.example.projectmanageweb.dto.GoalStats;
import com.example.projectmanageweb.dto.PersonalTaskStats;
import com.example.projectmanageweb.dto.RecentActivityDto;
import com.example.projectmanageweb.dto.UserProfileView;
import com.example.projectmanageweb.model.User;
import com.example.projectmanageweb.repository.GoalsRepository;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.ProjectsRepository;
import com.example.projectmanageweb.repository.TasksRepository;
import com.example.projectmanageweb.repository.UserRepository;

@Service
public class UserProfileService {
	private final UserRepository userRepo;
    private final ProjectsRepository projectsRepo;
    private final ProjectMembersRepository projectMembersRepo;
    private final TasksRepository tasksRepo;
    private final GoalsRepository goalsRepo;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public UserProfileService(UserRepository userRepo,
                              ProjectsRepository projectsRepo,
                              ProjectMembersRepository projectMembersRepo,
                              TasksRepository tasksRepo,
                              GoalsRepository goalsRepo) {
        this.userRepo = userRepo;
        this.projectsRepo = projectsRepo;
        this.projectMembersRepo = projectMembersRepo;
        this.tasksRepo = tasksRepo;
        this.goalsRepo = goalsRepo;
    }

    public UserProfileView loadProfile(int userId) {

        UserProfileView view = new UserProfileView();

        // ===== BASIC USER =====
        User user = userRepo.findById(userId);
        if (user == null) {
            return view; // hoặc throw
        }

        view.setUserId(user.getUserId());
        view.setFullName(user.getFullName());
        view.setEmail(user.getEmail());
        view.setAvatarUrl(user.getAvatarUrl());
        view.setPhoneNumber(user.getPhoneNumber());
        String globalRole = "User";
        if (user.getRoleId() != null) {
            if (user.getRoleId() == 1) {
                globalRole = "Admin";
            }
            // nếu sau này có thêm role khác (PM, Owner, v.v.) thì bổ sung case ở đây
        }
        view.setGlobalRole(globalRole);
        view.setWorkMode("Online");
        view.setWorkHoursPerWeek(40);

        // Extra profile (tạm để null, sau này có bảng user_profile thì map vào)
        view.setTitle(null);
        view.setDepartment(null);
        view.setOrganization(null);
        view.setLocation(null);

        // ===== SKILLS =====
        List<String> skills = projectMembersRepo.findAllSkillsOfUser(userId);
        view.setSkills(skills);

        // ===== PROJECTS LIST =====
        var projectList = projectsRepo.listForUser(userId, null, false);
        view.setActiveProjectsCount(projectList.size());

        // PM projects
        int pmProjects = projectMembersRepo.countPmProjects(userId);
        view.setPmProjectsCount(pmProjects);

        // ===== OPEN TASK COUNT =====
        int openTasks = tasksRepo.countOpenTasksOfUser(userId);
        view.setOpenTasksCount(openTasks);

        // Performance chưa có -> để null
        view.setPerformanceAvg(null);
        view.setLastFeedback(null);

        // ===== RECENT ACTIVITIES =====
        List<RecentActivityDto> activities = tasksRepo.findRecentActivitiesByUser(userId, 8);
        view.setRecentActivities(activities);
        view.setHasMoreActivities(activities.size() == 8); // đơn giản

        // ===== ACTIVE PROJECT ITEMS =====
        List<ActiveProjectItem> actProjects = new ArrayList<>();

        projectList.forEach(p -> {
            ActiveProjectItem item = new ActiveProjectItem();
            item.setProjectId(p.getProjectId());
            item.setProjectName(p.getProjectName());
            item.setStatus(p.getStatus());

            // role trong project
            var member = projectMembersRepo.findByUserAndProject(userId, p.getProjectId());
            item.setRoleInProject(member != null ? member.getRole() : "Member");

            // date range
            StringBuilder dr = new StringBuilder();
            if (p.getStartDate() != null) {
                dr.append(p.getStartDate().format(DATE_FMT));
            }
            if (p.getEndDate() != null) {
                dr.append(" - ").append(p.getEndDate().format(DATE_FMT));
            }
            item.setDateRange(dr.toString());

            // đếm task của user này trong project
            var mtasks = tasksRepo.findByProjectAndAssignee(p.getProjectId(), userId);
            int open = 0, done = 0;
            for (var t : mtasks) {
                if ("Done".equalsIgnoreCase(t.getStatus())) {
                    done++;
                } else {
                    open++;
                }
            }
            item.setOpenTasks(open);
            item.setDoneTasks(done);

            actProjects.add(item);
        });

        view.setActiveProjects(actProjects);
     // ----- Task stats cá nhân -----
        PersonalTaskStats pts = new PersonalTaskStats();
        // TODO: sau này có thể query DB để fill thật sự
        pts.setTodo(0);
        pts.setInProgress(0);
        pts.setReview(0);
        pts.setDone(0);

        view.setPersonalTaskStats(pts);


        // ===== GOAL STATS =====
        GoalStats gs = goalsRepo.getGoalStatsForUser(userId);
        view.setGoalStats(gs);

        // PersonalTaskStats hiện chưa có bảng -> để null
        view.setPersonalTaskStats(null);

        return view;
    }

}
