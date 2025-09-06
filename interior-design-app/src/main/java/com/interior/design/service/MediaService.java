package com.interior.design.service;

import com.interior.design.domain.Media;
import com.interior.design.domain.Project;
import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.dto.MediaDtos;
import com.interior.design.exception.ForbiddenOperationException;
import com.interior.design.exception.NotFoundException;
import com.interior.design.repository.MediaRepository;
import com.interior.design.repository.ProjectRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaService {

    private final ProjectRepository projectRepository;
    private final MediaRepository mediaRepository;
    private final CurrentUserService currentUserService;

    public MediaService(ProjectRepository projectRepository, MediaRepository mediaRepository, CurrentUserService currentUserService) {
        this.projectRepository = projectRepository;
        this.mediaRepository = mediaRepository;
        this.currentUserService = currentUserService;
    }

    public List<Media> list(Long projectId) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        return mediaRepository.findByProject(p);
    }

    @Transactional
    public Media upload(Long projectId, MediaDtos.UploadMediaRequest req) {
        Project p = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("Project not found"));
        User current = currentUserService.requireCurrentUser();
        if (current.getRole() == Role.VENDOR) {
            if (p.getVendor() == null || !p.getVendor().getId().equals(current.getId())) {
                throw new ForbiddenOperationException("You are not the vendor for this project");
            }
        } else if (current.getRole() != Role.ADMIN) {
            throw new ForbiddenOperationException("Not allowed");
        }
        Media m = new Media();
        m.setProject(p);
        m.setType(req.type());
        m.setUrl(req.url());
        return mediaRepository.save(m);
    }
}

