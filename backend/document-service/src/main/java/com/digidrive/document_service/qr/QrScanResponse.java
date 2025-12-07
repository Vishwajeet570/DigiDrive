package com.digidrive.document_service.qr;

import com.digidrive.document_service.vehichle.VehicleDTO;

import lombok.*;

// public record QrScanResponse(
//         Object vehicle,
//         Object documents
// ) {}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrScanResponse {
    private VehicleDTO vehicle;
    private Object owner;
}