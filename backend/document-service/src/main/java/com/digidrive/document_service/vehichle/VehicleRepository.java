package com.digidrive.document_service.vehichle;

import com.digidrive.document_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByOwner(User owner);

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);
}
