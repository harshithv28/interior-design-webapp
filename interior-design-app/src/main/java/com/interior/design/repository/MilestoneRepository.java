package com.interior.design.repository;

import com.interior.design.domain.Milestone;
import com.interior.design.domain.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByProject(Project project);
}

