package com.interior.design.controller;

import com.interior.design.service.EstimationService;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estimate")
public class EstimationController {

    private final EstimationService estimationService;

    public EstimationController(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @GetMapping("/sqft")
    public ResponseEntity<Map<String, Object>> bySqft(@RequestParam int sqft) {
        BigDecimal cost = estimationService.estimateBySquareFootage(sqft);
        return ResponseEntity.ok(Map.of("sqft", sqft, "estimatedCost", cost));
    }

    @GetMapping("/room")
    public ResponseEntity<Map<String, Object>> byRoom(@RequestParam String roomType, @RequestParam int sqft) {
        BigDecimal cost = estimationService.estimateByRoomType(roomType, sqft);
        return ResponseEntity.ok(Map.of("roomType", roomType, "sqft", sqft, "estimatedCost", cost));
    }
}

