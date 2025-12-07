package com.digidrive.document_service.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.Instant;

@Document(collection = "documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentMetadata {

    @Id
    private String id;               // Mongo document id

    private String gridFsFileId;     // GridFS stored file id (ObjectId as String)
    private String filename;
    private String contentType;
    private Long vehicleId;          // Postgres vehicle id this doc belongs to
    private String uploadedBy;       // user email who uploaded
    private String docType;          // RC / INSURANCE / PUC / LICENSE
    private String status;           // PENDING / APPROVED / REJECTED
    private Instant uploadedAt;
    private Instant expiryDate;      // optional: parse from doc if available
    private String notes;            // optional admin notes
}
