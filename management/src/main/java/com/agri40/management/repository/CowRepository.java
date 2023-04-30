package com.agri40.management.repository;

import com.agri40.management.domain.Cow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Cow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CowRepository extends MongoRepository<Cow, String> {

    List<Cow> findByEnclosId(String id);

    List<Cow> findByWaitingForInseminator(boolean b);

    Optional<Cow> findByRfidOrCollarOrPedometre(String deviceid, String deviceid2, String deviceid3);
}
