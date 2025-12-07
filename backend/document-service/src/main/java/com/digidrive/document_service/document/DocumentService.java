package com.digidrive.document_service.document;

import com.digidrive.document_service.vehichle.VehicleRepository;
import com.digidrive.document_service.vehichle.Vehicle;
import com.digidrive.document_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.gridfs.model.GridFSFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final GridFsTemplate gridFsTemplate;
    private final DocumentRepository docRepo;
    private final VehicleRepository vehicleRepo;
    private final UserRepository userRepo;

    public DocumentMetadata upload(Long vehicleId, MultipartFile file, String docType, String uploaderEmail) throws IOException {
        // 1. verify vehicle exists
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // 2. store file bytes in GridFS
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());

        // 3. build metadata
        DocumentMetadata meta = DocumentMetadata.builder()
                .gridFsFileId(fileId.toHexString())
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .vehicleId(vehicleId)
                .uploadedBy(uploaderEmail)
                .docType(docType)
                .status("PENDING")
                .uploadedAt(Instant.now())
                .build();

        return docRepo.save(meta);
    }

    public List<DocumentMetadata> listByVehicle(Long vehicleId) {
        return docRepo.findByVehicleId(vehicleId);
    }

    public Optional<GridFsResource> fetchFileResource(String documentId) throws IOException {
        DocumentMetadata meta = docRepo.findById(documentId).orElseThrow(() -> new RuntimeException("Doc meta not found"));
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(meta.getGridFsFileId()))));
        if (file == null) return Optional.empty();
        GridFsResource resource = gridFsTemplate.getResource(file);
        return Optional.of(resource);
    }

    public DocumentMetadata updateStatus(String documentId, String status, String notes) {
        DocumentMetadata meta = docRepo.findById(documentId).orElseThrow(() -> new RuntimeException("Doc not found"));
        meta.setStatus(status);
        if (notes != null) meta.setNotes(notes);
        return docRepo.save(meta);
    }
}
