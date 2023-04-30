package com.agri40.management.repository;

import com.agri40.management.domain.Events;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Events entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventsRepository extends MongoRepository<Events, String> {}
