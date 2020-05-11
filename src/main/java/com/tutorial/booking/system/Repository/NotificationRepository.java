/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * <p>A repository for the notification table on the database. All database requests for that table go through this class.</p>
 * @author Jonathan Watt
 */
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    /**
     * <p>Finds a list of notifications for a users ordered by the created on starting from most recent.</p>
     * @param user - The user to find the notifications for.
     * @return - The list of notifications found.
     */
    List<Notification> findAllByUserIdOrderByCreatedOnDesc(User user);

    /**
     * <p>Finds a list of notifications for a user which are not seen.</p>
     * @param user - The user to get the notifications for.
     * @return - The list of unseen notifications for a user.
     */
    List<Notification> findAllByUserIdAndSeenIsFalse(User user);
}
