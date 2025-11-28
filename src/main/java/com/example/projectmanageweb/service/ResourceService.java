package com.example.projectmanageweb.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectmanageweb.dto.ResourceItemDto;
import com.example.projectmanageweb.model.ProjectResource;
import com.example.projectmanageweb.repository.ProjectMembersRepository;
import com.example.projectmanageweb.repository.ProjectResourcesRepository;
import com.example.projectmanageweb.repository.TaskAssigneesRepository;
import com.example.projectmanageweb.repository.TasksRepository;

import java.util.UUID;

@Service
public class ResourceService {

    private final ProjectResourcesRepository resourcesRepo;
    private final ProjectMembersRepository membersRepo;
    private final TaskAssigneesRepository assigneesRepo;
    private final TasksRepository tasksRepo;

    private final Path baseDir = Paths.get("uploads/resources");

    public ResourceService(ProjectResourcesRepository resourcesRepo,
                           ProjectMembersRepository membersRepo,
                           TaskAssigneesRepository assigneesRepo,
                           TasksRepository tasksRepo) {
        this.resourcesRepo = resourcesRepo;
        this.membersRepo = membersRepo;
        this.assigneesRepo = assigneesRepo;
        this.tasksRepo = tasksRepo;
    }

    // Upload file cho task
    public void uploadForTask(int projectId,
                              int taskId,
                              int uploaderId,
                              MultipartFile file,
                              String description) throws IOException {

        if (file == null || file.isEmpty()) return;

        var task = tasksRepo.findById(taskId);
        if (task == null || task.getProjectId() != projectId) {
            throw new IllegalArgumentException("Task không thuộc project.");
        }

        boolean isPm = membersRepo.isPmOfProject(projectId, uploaderId);
        boolean isAssignee = assigneesRepo.isAssignee(taskId, uploaderId);

        if (!isPm && !isAssignee) {
            throw new AccessDeniedException("Không được upload file.");
        }

        Path dir = baseDir.resolve("project-" + projectId).resolve("task-" + taskId);
        Files.createDirectories(dir);

        String original = file.getOriginalFilename();
        String ext = "";

        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }

        String stored = UUID.randomUUID().toString().replace("-", "") + ext;
        Path target = dir.resolve(stored);
        Files.copy(file.getInputStream(), target);

        ProjectResource r = new ProjectResource();
        r.setProjectId(projectId);
        r.setTaskId(taskId);
        r.setUploaderId(uploaderId);
        r.setOriginalName(original);
        r.setStoredName(stored);
        r.setContentType(file.getContentType());
        r.setSizeBytes(file.getSize());
        r.setDescription(description);
        r.setUploadedAt(LocalDateTime.now());

        resourcesRepo.insert(r);
    }

    // List file theo project
    public List<ResourceItemDto> listByProject(int projectId) {
        return resourcesRepo.findByProject(projectId);
    }

    public ProjectResource getResource(int resourceId) {
        return resourcesRepo.findById(resourceId);
    }

    public Path resolveFilePath(ProjectResource r) {
        Path dir = baseDir
                .resolve("project-" + r.getProjectId())
                .resolve("task-" + r.getTaskId());
        return dir.resolve(r.getStoredName());
    }

    public void deleteResource(int projectId, int userId, int resourceId) {
        ProjectResource r = resourcesRepo.findById(resourceId);
        if (r == null || r.getProjectId() != projectId) {
            throw new IllegalArgumentException("Resource không hợp lệ");
        }

        boolean isPm = membersRepo.isPmOfProject(projectId, userId);
        boolean isOwner = r.getUploaderId() == userId;

        if (!isPm && !isOwner) {
            throw new AccessDeniedException("Không có quyền xoá file.");
        }

        try {
            Files.deleteIfExists(resolveFilePath(r));
        } catch (IOException ignore) {}

        resourcesRepo.deleteById(resourceId);
    }
}
