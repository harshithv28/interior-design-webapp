package com.interior.design.service;

import com.interior.design.domain.Notification;
import com.interior.design.domain.Project;
import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.exception.ForbiddenOperationException;
import com.interior.design.exception.NotFoundException;
import com.interior.design.repository.NotificationRepository;
import com.interior.design.repository.ProjectRepository;
import com.interior.design.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public NotificationService(NotificationRepository notificationRepository, ProjectRepository projectRepository, UserRepository userRepository, CurrentUserService currentUserService) {
        this.notificationRepository = notificationRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public List<Notification> listForProject(Long projectId) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        return notificationRepository.findByProject(p);
    }

    public List<Notification> listForUser() {
        User current = currentUserService.requireCurrentUser();
        return notificationRepository.findByRecipient(current);
    }

    @Transactional
    public Notification notifyCustomer(Long projectId, String message) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        User current = currentUserService.requireCurrentUser();
        if (current.getRole() == Role.VENDOR) {
            if (p.getVendor() == null || !p.getVendor().getId().equals(current.getId())) {
                throw new ForbiddenOperationException("You are not the vendor for this project");
            }
        } else if (current.getRole() != Role.ADMIN) {
            throw new ForbiddenOperationException("Not allowed");
        }
        Notification n = new Notification();
        n.setProject(p);
        n.setRecipient(p.getCustomer());
        n.setMessage(message);
        return notificationRepository.save(n);
    }
}

