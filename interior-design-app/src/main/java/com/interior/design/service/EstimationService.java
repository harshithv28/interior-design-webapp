package com.interior.design.service;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EstimationService {

    private static final Map<String, BigDecimal> ROOM_BASE_COST = Map.of(
            "LIVING_ROOM", new BigDecimal("25.0"),
            "BEDROOM", new BigDecimal("22.0"),
            "KITCHEN", new BigDecimal("35.0"),
            "BATHROOM", new BigDecimal("40.0")
    );

    public BigDecimal estimateBySquareFootage(int squareFeet) {
        BigDecimal rate = new BigDecimal("20.0");
        return rate.multiply(BigDecimal.valueOf(squareFeet));
    }

    public BigDecimal estimateByRoomType(String roomType, int squareFeet) {
        BigDecimal rate = ROOM_BASE_COST.getOrDefault(roomType.toUpperCase(), new BigDecimal("20.0"));
        return rate.multiply(BigDecimal.valueOf(squareFeet));
    }
}

