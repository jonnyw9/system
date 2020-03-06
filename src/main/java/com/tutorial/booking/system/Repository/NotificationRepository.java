package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
