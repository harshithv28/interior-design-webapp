package com.interior.design.dto;

import java.math.BigDecimal;

public class ProjectDtos {
    public record CreateProjectRequest(String name, String description, Long customerId) {}
    public record ProjectResponse(Long id, String name, String description, Long customerId, Long vendorId, BigDecimal estimatedCost) {}
}

