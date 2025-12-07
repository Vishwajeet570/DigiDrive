package com.digidrive.document_service.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DocumentRepository extends MongoRepository<DocumentMetadata, String> {
    List<DocumentMetadata> findByVehicleId(Long vehicleId);
    List<DocumentMetadata> findByUploadedBy(String uploadedBy);
}
