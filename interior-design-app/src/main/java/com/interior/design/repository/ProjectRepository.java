package com.interior.design.repository;

import com.interior.design.domain.Project;
import com.interior.design.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCustomer(User customer);
    List<Project> findByVendor(User vendor);
}

