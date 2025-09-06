package com.interior.design.repository;

import com.interior.design.domain.Notification;
import com.interior.design.domain.Project;
import com.interior.design.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByProject(Project project);
    List<Notification> findByRecipient(User user);
}

