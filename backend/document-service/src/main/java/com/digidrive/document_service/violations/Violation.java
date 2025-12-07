package com.digidrive.document_service.violations;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "violations")
public class Violation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;
    private Long userId; // vehicle owner
    private String userEmail;
    private String reason;
    private Integer fineAmount;
    private String location;

    private boolean paid = false;

    private LocalDateTime issuedAt = LocalDateTime.now();
    private LocalDateTime paidAt;
}
