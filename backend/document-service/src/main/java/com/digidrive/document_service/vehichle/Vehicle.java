package com.digidrive.document_service.vehichle;

import com.digidrive.document_service.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleNumber;
    private String model;
    private String brand;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner; // linked to logged-in user
}
