package com.interior.design.controller;

import com.interior.design.domain.Milestone;
import com.interior.design.dto.MilestoneDtos;
import com.interior.design.service.MilestoneService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/milestone")
public class MilestoneController {

    private final MilestoneService milestoneService;

    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @GetMapping("s")
    public ResponseEntity<List<Milestone>> list(@PathVariable Long projectId) {
        return ResponseEntity.ok(milestoneService.list(projectId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<Milestone> create(@PathVariable Long projectId, @RequestBody MilestoneDtos.CreateMilestoneRequest req) {
        return ResponseEntity.ok(milestoneService.create(projectId, req));
    }

    @PatchMapping("/{milestoneId}")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<Milestone> update(@PathVariable Long projectId, @PathVariable Long milestoneId, @RequestBody MilestoneDtos.UpdateMilestoneStatusRequest req) {
        return ResponseEntity.ok(milestoneService.updateStatus(projectId, milestoneId, req));
    }
}

