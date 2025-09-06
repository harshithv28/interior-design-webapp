package com.interior.design.controller;

import com.interior.design.domain.Project;
import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.exception.NotFoundException;
import com.interior.design.repository.ProjectRepository;
import com.interior.design.repository.UserRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public AdminController(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> projects() {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    @PostMapping("/projects/{projectId}/assign/{vendorId}")
    public ResponseEntity<Project> assignVendor(@PathVariable Long projectId, @PathVariable Long vendorId) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        User v = userRepository.findById(vendorId).orElseThrow(() -> new NotFoundException("Vendor not found"));
        if (v.getRole() != Role.VENDOR) {
            throw new IllegalArgumentException("Assigned user is not a vendor");
        }
        p.setVendor(v);
        return ResponseEntity.ok(projectRepository.save(p));
    }
}

