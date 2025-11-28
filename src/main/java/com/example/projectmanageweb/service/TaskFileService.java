package com.example.projectmanageweb.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectmanageweb.model.TaskFile;
import com.example.projectmanageweb.repository.TaskFilesRepository;



@Service
public class TaskFileService {

	private final TaskFilesRepository filesRepo;

    // Thư mục gốc để lưu file, có thể cấu hình trong application.properties
    // app.upload.base-dir=uploads
    @Value("${app.upload.base-dir:uploads}")
    private String baseDir;

    public TaskFileService(TaskFilesRepository filesRepo) {
        this.filesRepo = filesRepo;
    }

    public List<TaskFile> saveFilesForTask(
            int projectId,
            int taskId,
            int uploaderId,
            List<MultipartFile> files
    ) throws IOException {

        List<TaskFile> result = new ArrayList<>();
        if (files == null || files.isEmpty()) return result;

        Path projectDir = Path.of(baseDir, "project-" + projectId, "task-" + taskId);
        Files.createDirectories(projectDir);

        for (MultipartFile mf : files) {
            if (mf == null || mf.isEmpty()) continue;

            String original = mf.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }

            String stored = UUID.randomUUID().toString().replace("-", "") + ext;
            Path target = projectDir.resolve(stored);

            Files.copy(mf.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            TaskFile f = new TaskFile();
            f.setProjectId(projectId);
            f.setTaskId(taskId);
            f.setUploaderId(uploaderId);
            f.setOriginalName(original);
            f.setStoredName(stored);
            f.setContentType(mf.getContentType());
            f.setSize(mf.getSize());
            f.setUploadedAt(LocalDateTime.now());

            int id = filesRepo.insert(f);
            f.setFileId(id);
            result.add(f);
        }

        return result;
    }

    public List<TaskFile> listByTask(int projectId, int taskId) {
        return filesRepo.findByTask(projectId, taskId);
    }

    public List<TaskFile> listByProject(int projectId) {
        return filesRepo.findByProject(projectId);
    }

    public TaskFile findById(int fileId) {
        return filesRepo.findById(fileId);
    }

    public Path resolvePhysicalPath(TaskFile f) {
        return Path.of(baseDir,
                "project-" + f.getProjectId(),
                "task-" + f.getTaskId(),
                f.getStoredName());
    }
}
