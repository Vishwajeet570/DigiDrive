package com.digidrive.document_service.vehichle;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public Vehicle registerVehicle(Authentication auth, @RequestBody Vehicle vehicle) {
        return service.registerVehicle(auth.getName(), vehicle);
    }

    @GetMapping("/my")
    public List<Vehicle> getMyVehicles(Authentication auth) {
        return service.getMyVehicles(auth.getName());
    }

    @GetMapping("/all")
    public List<VehicleDTO> getAll() {
        return service.getAllVehicles();
    }

    @GetMapping("/{number}")
    public Vehicle getByNumber(@PathVariable String number) {
        return service.getVehicleByNumber(number);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return service.updateVehicle(id, vehicle);
    }

    @DeleteMapping("/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        service.deleteVehicle(id);
        return "Deleted";
    }
}
