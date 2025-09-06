package com.interior.design.controller;

import com.interior.design.domain.Project;
import com.interior.design.exception.NotFoundException;
import com.interior.design.repository.ProjectRepository;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final ProjectRepository projectRepository;

    public DashboardController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/customer/{projectId}")
    public ResponseEntity<Map<String, Object>> customer(@PathVariable Long projectId) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        return ResponseEntity.ok(Map.of(
                "projectId", p.getId(),
                "name", p.getName(),
                "description", p.getDescription(),
                "estimatedCost", p.getEstimatedCost(),
                "role", "CUSTOMER"
        ));
    }

    @GetMapping("/vendor/{projectId}")
    public ResponseEntity<Map<String, Object>> vendor(@PathVariable Long projectId) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        return ResponseEntity.ok(Map.of(
                "projectId", p.getId(),
                "name", p.getName(),
                "description", p.getDescription(),
                "estimatedCost", p.getEstimatedCost(),
                "role", "VENDOR"
        ));
    }
}

