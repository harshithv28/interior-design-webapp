package com.interior.design.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/dashboard")
public class DashboardAliasController {
    @GetMapping("/customer")
    public ResponseEntity<String> customer() {
        return ResponseEntity.ok("Customer dashboard endpoint is available under /dashboard/customer/{projectId}");
    }
    @GetMapping("/vendor")
    public ResponseEntity<String> vendor() {
        return ResponseEntity.ok("Vendor dashboard endpoint is available under /dashboard/vendor/{projectId}");
    }
}

