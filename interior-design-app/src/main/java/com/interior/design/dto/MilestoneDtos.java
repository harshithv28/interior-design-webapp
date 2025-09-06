package com.interior.design.dto;

import com.interior.design.domain.Milestone;

public class MilestoneDtos {
    public record CreateMilestoneRequest(String title, String description) {}
    public record UpdateMilestoneStatusRequest(Milestone.Status status, String description) {}
}

