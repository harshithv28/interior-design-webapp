package com.interior.design.repository;

import com.interior.design.domain.Media;
import com.interior.design.domain.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByProject(Project project);
}

