package com.digidrive.document_service.vehichle;

import lombok.Data;

@Data
public class VehicleDTO {
    private Long id;
    private String vehicleNumber;
    private String brand;
    private String model;
    private String color;
    private Long ownerId;
}

