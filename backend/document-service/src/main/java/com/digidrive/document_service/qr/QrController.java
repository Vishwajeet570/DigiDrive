package com.digidrive.document_service.qr;

import com.digidrive.document_service.vehichle.VehicleDTO;
import com.digidrive.document_service.vehichle.VehicleRepository;
import com.digidrive.document_service.document.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QrController {
    
    private final QrService qrService;
    private final QrTokenUtil qrTokenUtil;
    private final VehicleRepository vehicleRepo;
    private final DocumentRepository docRepo;

    // USER generates QR for OWN vehicle
    @GetMapping("/generate/{vehicleNumber}")
    public ResponseEntity<?> generate(
            @PathVariable String vehicleNumber,
            Authentication auth
    ) throws Exception {

        String loggedInUserId = auth.getName();

        // Fetch vehicle
        var vehicle = vehicleRepo.findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // ensure user owns the vehicle
        if (!vehicle.getOwner().getEmail().equals(loggedInUserId)) {
            return ResponseEntity.status(403).body("You cannot generate QR for another user’s vehicle");
        }

        String qrToken = qrTokenUtil.generateToken(vehicleNumber, vehicle.getOwner().getId());

        var image = qrService.generate(qrToken);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);

        return ResponseEntity.ok()
                .header("X-QR-TOKEN", qrToken)
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
    }

    // POLICE scans QR
    @GetMapping("/scan")
    public ResponseEntity<?> scan(@RequestParam String token) {

        QrTokenData data = qrTokenUtil.validate(token);

        var vehicle = vehicleRepo.findByVehicleNumber(data.vehicleNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        VehicleDTO vdto = new VehicleDTO();
        vdto.setBrand(vehicle.getBrand());
        vdto.setId(vehicle.getId());
        vdto.setOwnerId(vehicle.getOwner().getId());
        vdto.setColor(vehicle.getColor());
        vdto.setModel(vehicle.getModel());
        vdto.setVehicleNumber(vehicle.getVehicleNumber());


        var docs = docRepo.findByVehicleId(vehicle.getId())
                .stream()
                .filter(d -> d.getStatus().equals("APPROVED"))
                .toList();

        return ResponseEntity.ok(new QrScanResponse(vdto, docs));
    }
}
