package com.digidrive.document_service.violations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/violations")
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationService violationService;

    // POLICE creates violation
    @PostMapping("/create")
    public ResponseEntity<?> createViolation(@RequestBody ViolationRequest req) {
        return ResponseEntity.ok(violationService.createViolation(req));
    }

    // USER gets own violations
    @GetMapping("/my")
    public ResponseEntity<?> getMyViolations(Authentication auth) {

        String userId = auth.getName();

        return ResponseEntity.ok(violationService.getUserViolations(userId));
    }

    // USER pays challan
    @PostMapping("/pay/{id}")
    public ResponseEntity<?> payViolation(
            @PathVariable Long id,
            Authentication auth
    ) {

        String userId = auth.getName();
        return ResponseEntity.ok(violationService.payViolation(id, userId));
    }

    // ADMIN gets all
    @GetMapping("/all")
    public ResponseEntity<?> getAllViolations() {
        return ResponseEntity.ok(violationService.getAllViolations());
    }
}
