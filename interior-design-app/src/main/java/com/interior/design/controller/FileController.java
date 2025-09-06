package com.interior.design.controller;

import com.interior.design.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    private final FileStorageService storageService;

    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/files/upload")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile file) {
        String name = storageService.store(file);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> get(@PathVariable String filename) {
        Resource res = storageService.loadAsResource(filename);
        String contentType = storageService.detectContentType(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .body(res);
    }
}

