package com.interior.design.controller;

import com.interior.design.domain.Quotation;
import com.interior.design.dto.QuotationDtos;
import com.interior.design.service.QuotationService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/quote")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @GetMapping
    public ResponseEntity<List<Quotation>> list(@PathVariable Long projectId) {
        return ResponseEntity.ok(quotationService.getForProject(projectId));
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<Quotation> update(@PathVariable Long projectId, @RequestBody QuotationDtos.UpdateQuoteRequest req) {
        return ResponseEntity.ok(quotationService.updateQuote(projectId, req));
    }

    @PostMapping("/respond")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Quotation> respond(@RequestParam Long quotationId, @RequestParam boolean accept) {
        return ResponseEntity.ok(quotationService.respond(quotationId, accept));
    }
}

