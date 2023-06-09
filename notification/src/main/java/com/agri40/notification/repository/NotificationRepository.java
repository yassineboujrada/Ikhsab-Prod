package com.agri40.notification.repository;

import com.agri40.notification.domain.Notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Page<Notification> findAllBySeenIsFalse(Pageable pageable);

    List<Notification> findAllByReceiverAndSeen(String id, boolean b);

    Notification findFirstByCowIdAndSeenOrderByDateDesc(String cowId, boolean b);

    Notification findFirstByCowIdOrderByDateDesc(String string);
}
