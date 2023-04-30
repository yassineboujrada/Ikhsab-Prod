package com.agri40.management.repository;

import com.agri40.management.domain.Calendar;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Calendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarRepository extends MongoRepository<Calendar, String> {
    List<Calendar> findAllByCowId(String id);
}
