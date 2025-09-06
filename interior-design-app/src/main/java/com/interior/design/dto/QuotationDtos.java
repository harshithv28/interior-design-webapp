package com.interior.design.dto;

import java.math.BigDecimal;

public class QuotationDtos {
    public record UpdateQuoteRequest(BigDecimal materialsCost, BigDecimal laborCost, BigDecimal customItemsCost) {}
}

