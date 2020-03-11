package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByUserIdOrderByCreatedOnDesc(User user);

    List<Notification> findAllByUserIdAndSeenIsFalse(User user);
}
