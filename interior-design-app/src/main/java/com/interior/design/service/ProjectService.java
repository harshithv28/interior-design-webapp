package com.interior.design.service;

import com.interior.design.domain.Project;
import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.dto.ProjectDtos;
import com.interior.design.exception.ForbiddenOperationException;
import com.interior.design.exception.NotFoundException;
import com.interior.design.repository.ProjectRepository;
import com.interior.design.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, CurrentUserService currentUserService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public List<ProjectDtos.ProjectResponse> listProjects() {
        User current = currentUserService.requireCurrentUser();
        List<Project> source;
        if (current.getRole() == Role.ADMIN) {
            source = projectRepository.findAll();
        } else if (current.getRole() == Role.VENDOR) {
            source = projectRepository.findByVendor(current);
        } else {
            source = projectRepository.findByCustomer(current);
        }
        return source.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ProjectDtos.ProjectResponse createProject(ProjectDtos.CreateProjectRequest request) {
        User current = currentUserService.requireCurrentUser();
        if (current.getRole() != Role.VENDOR && current.getRole() != Role.ADMIN) {
            throw new ForbiddenOperationException("Only vendors/admins can create projects");
        }
        User customer = userRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Provided user is not a customer");
        }
        Project p = new Project();
        p.setName(request.name());
        p.setDescription(request.description());
        p.setCustomer(customer);
        p.setVendor(current.getRole() == Role.VENDOR ? current : null);
        p.setEstimatedCost(BigDecimal.ZERO);
        return toResponse(projectRepository.save(p));
    }

    public Project requireProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }

    private ProjectDtos.ProjectResponse toResponse(Project p) {
        return new ProjectDtos.ProjectResponse(
                p.getId(), p.getName(), p.getDescription(),
                p.getCustomer() != null ? p.getCustomer().getId() : null,
                p.getVendor() != null ? p.getVendor().getId() : null,
                p.getEstimatedCost()
        );
    }
}

