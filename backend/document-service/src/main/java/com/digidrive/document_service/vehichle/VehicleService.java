package com.digidrive.document_service.vehichle;

import com.digidrive.document_service.entity.User;
import com.digidrive.document_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository repo;
    private final UserRepository userRepo;

    public VehicleService(VehicleRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public Vehicle registerVehicle(String email, Vehicle vehicle) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        vehicle.setOwner(user);
        return repo.save(vehicle);
    }

    public List<Vehicle> getMyVehicles(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repo.findByOwner(user);
    }

    // public List<Vehicle> getAllVehicles() {
    //     return repo.findAll();
    // }

    public Vehicle getVehicleByNumber(String number) {
        return repo.findByVehicleNumber(number)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        existing.setBrand(updated.getBrand());
        existing.setColor(updated.getColor());
        existing.setModel(updated.getModel());
        existing.setVehicleNumber(updated.getVehicleNumber());

        return repo.save(existing);
    }

    public void deleteVehicle(Long id) {
        repo.deleteById(id);
    }

    public List<VehicleDTO> getAllVehicles() {
    return repo.findAll().stream()
        .map(v -> {
            VehicleDTO dto = new VehicleDTO();
            dto.setId(v.getId());
            dto.setVehicleNumber(v.getVehicleNumber());
            dto.setBrand(v.getBrand());
            dto.setModel(v.getModel());
            dto.setColor(v.getColor());
            dto.setOwnerId(v.getOwner().getId());
            return dto;
        })
        .toList();
}

}
