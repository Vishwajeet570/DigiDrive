package com.digidrive.document_service.violations;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ViolationRepository extends JpaRepository<Violation, Long> {
    List<Violation> findByUserEmail(String userEmail);

}
