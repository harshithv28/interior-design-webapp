package com.interior.design.service;

import com.interior.design.domain.Milestone;
import com.interior.design.domain.Project;
import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.dto.MilestoneDtos;
import com.interior.design.exception.ForbiddenOperationException;
import com.interior.design.exception.NotFoundException;
import com.interior.design.repository.MilestoneRepository;
import com.interior.design.repository.ProjectRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;

    public MilestoneService(MilestoneRepository milestoneRepository, ProjectRepository projectRepository, CurrentUserService currentUserService) {
        this.milestoneRepository = milestoneRepository;
        this.projectRepository = projectRepository;
        this.currentUserService = currentUserService;
    }

    public List<Milestone> list(Long projectId) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        return milestoneRepository.findByProject(p);
    }

    @Transactional
    public Milestone create(Long projectId, MilestoneDtos.CreateMilestoneRequest req) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        User current = currentUserService.requireCurrentUser();
        if (current.getRole() == Role.VENDOR) {
            if (p.getVendor() == null || !p.getVendor().getId().equals(current.getId())) {
                throw new ForbiddenOperationException("You are not the vendor for this project");
            }
        } else if (current.getRole() != Role.ADMIN) {
            throw new ForbiddenOperationException("Not allowed");
        }
        Milestone m = new Milestone();
        m.setProject(p);
        m.setTitle(req.title());
        m.setDescription(req.description());
        m.setStatus(Milestone.Status.PENDING);
        return milestoneRepository.save(m);
    }

    @Transactional
    public Milestone updateStatus(Long projectId, Long milestoneId, MilestoneDtos.UpdateMilestoneStatusRequest req) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        Milestone m = milestoneRepository.findById(milestoneId).orElseThrow(() -> new NotFoundException("Milestone not found"));
        if (!m.getProject().getId().equals(p.getId())) {
            throw new IllegalArgumentException("Milestone does not belong to project");
        }
        User current = currentUserService.requireCurrentUser();
        if (current.getRole() == Role.VENDOR) {
            if (p.getVendor() == null || !p.getVendor().getId().equals(current.getId())) {
                throw new ForbiddenOperationException("You are not the vendor for this project");
            }
        } else if (current.getRole() != Role.ADMIN) {
            throw new ForbiddenOperationException("Not allowed");
        }
        if (req.status() != null) m.setStatus(req.status());
        if (req.description() != null) m.setDescription(req.description());
        return milestoneRepository.save(m);
    }
}

