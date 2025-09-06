package com.interior.design.controller;

import com.interior.design.domain.Media;
import com.interior.design.dto.MediaDtos;
import com.interior.design.service.MediaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    public ResponseEntity<List<Media>> list(@PathVariable Long projectId) {
        return ResponseEntity.ok(mediaService.list(projectId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<Media> upload(@PathVariable Long projectId, @RequestBody MediaDtos.UploadMediaRequest req) {
        return ResponseEntity.ok(mediaService.upload(projectId, req));
    }
}

