package com.interior.design.controller;

import com.interior.design.dto.ProjectDtos;
import com.interior.design.service.ProjectService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDtos.ProjectResponse>> list() {
        return ResponseEntity.ok(projectService.listProjects());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<ProjectDtos.ProjectResponse> create(@RequestBody ProjectDtos.CreateProjectRequest req) {
        return ResponseEntity.ok(projectService.createProject(req));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDtos.ProjectResponse> get(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.listProjects().stream()
                .filter(p -> p.id().equals(projectId))
                .findFirst()
                .orElseThrow());
    }
}

