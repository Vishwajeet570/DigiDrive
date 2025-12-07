package com.digidrive.document_service.document;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // Upload: ROLE_USER (owner) or ROLE_ADMIN
    @PostMapping("/upload/{vehicleId}")
    public ResponseEntity<DocumentMetadata> upload(
            Authentication auth,
            @PathVariable Long vehicleId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("docType") String docType) throws IOException {

        String email = auth.getName();
        DocumentMetadata meta = documentService.upload(vehicleId, file, docType, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(meta);
    }

    // List metadata for a vehicle: owner / admin / police can view
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<DocumentMetadata>> listByVehicle(@PathVariable Long vehicleId) {
        List<DocumentMetadata> list = documentService.listByVehicle(vehicleId);
        return ResponseEntity.ok(list);
    }

    // Download document by metadata id: only allowed roles (Admin/Police/Owner). Gateway or filter should verify.
    @GetMapping("/{documentId}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable String documentId) throws IOException {
        var opt = documentService.fetchFileResource(documentId);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        var resource = opt.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(resource.contentLength());
        headers.setContentType(MediaType.parseMediaType(resource.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(resource.getFilename()).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(resource.getInputStream()));
    }

    // Admin approves/rejects
    @PatchMapping("/{documentId}/status")
    public ResponseEntity<DocumentMetadata> updateStatus(@PathVariable String documentId,
                                                         @RequestParam String status,
                                                         @RequestParam(required = false) String notes) {
        DocumentMetadata updated = documentService.updateStatus(documentId, status, notes);
        return ResponseEntity.ok(updated);
    }
}
