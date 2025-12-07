package com.digidrive.document_service.violations;

import com.digidrive.document_service.vehichle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolationService {

    private final ViolationRepository violationRepo;
    private final VehicleRepository vehicleRepo;

    // Police creates violation
    public Violation createViolation(ViolationRequest req) {

        var vehicle = vehicleRepo.findById(req.vehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Violation v = new Violation();
        v.setVehicleId(req.vehicleId());
        v.setUserEmail(vehicle.getOwner().getEmail());
        v.setUserId(vehicle.getOwner().getId()); // vehicle owner
        v.setReason(req.reason());
        v.setFineAmount(req.fineAmount());
        v.setLocation(req.location());

        return violationRepo.save(v);
    }

    // User views own
    public List<Violation> getUserViolations(String userId) {
        return violationRepo.findByUserEmail(userId);
    }

    // Admin view all
    public List<Violation> getAllViolations() {
        return violationRepo.findAll();
    }

    // User pays fine
    public Violation payViolation(Long id, String userId) {

        var v = violationRepo.findByUserEmail(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Violation not found"));

        var vehicle = vehicleRepo.findById(v.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));        
        

        if (!v.getUserId().equals(vehicle.getOwner().getId())) {
            throw new RuntimeException("You cannot pay someone else's challan");
        }

        v.setPaid(true);
        v.setPaidAt(LocalDateTime.now());

        return violationRepo.save(v);
    }
}
