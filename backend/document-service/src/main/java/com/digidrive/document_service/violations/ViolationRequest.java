package com.digidrive.document_service.violations;

public record ViolationRequest(
        Long vehicleId,
        Long userId,
        String userEmail,
        String reason,
        Integer fineAmount,
        String location
) {}
