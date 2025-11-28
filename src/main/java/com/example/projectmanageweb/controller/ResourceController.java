package com.example.projectmanageweb.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.projectmanageweb.model.ProjectResource;
import com.example.projectmanageweb.service.ResourceService;
import com.example.projectmanageweb.service.UserService;

@Controller
public class ResourceController {

	private final ResourceService resourceService;
    private final UserService userService;

    public ResourceController(ResourceService resourceService, UserService userService) {
        this.resourceService = resourceService;
        this.userService = userService;
    }

    @GetMapping("/projects/{projectId}/resources/{resourceId}/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> download(
            @PathVariable int projectId,
            @PathVariable int resourceId,
            Authentication auth
    ) throws IOException {

        var me = userService.findByEmail(auth.getName()).orElseThrow();

        ProjectResource r = resourceService.getResource(resourceId);
        if (r == null || r.getProjectId() != projectId) {
            return ResponseEntity.notFound().build();
        }

        var path = resourceService.resolveFilePath(r);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        var in = Files.newInputStream(path);
        var resource = new InputStreamResource(in);

        String contentType = r.getContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + r.getOriginalName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(r.getSizeBytes())
                .body(resource);
    }

    @PostMapping("/projects/{projectId}/resources/{resourceId}/delete")
    @PreAuthorize("isAuthenticated()")
    public String delete(
            @PathVariable int projectId,
            @PathVariable int resourceId,
            Authentication auth,
            RedirectAttributes ra
    ) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();
        try {
            resourceService.deleteResource(projectId, me.getUserId(), resourceId);
            ra.addFlashAttribute("successMessage", "Xoá file thành công.");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/projects/" + projectId + "#resources-content";
    }
}
