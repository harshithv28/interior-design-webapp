package com.interior.design.repository;

import com.interior.design.domain.Project;
import com.interior.design.domain.Quotation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByProject(Project project);
}

