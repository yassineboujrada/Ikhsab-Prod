package com.agri40.sensoring.repository;

import com.agri40.sensoring.domain.Chaleurs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Chaleurs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChaleursRepository extends MongoRepository<Chaleurs, String> {
    Optional<Chaleurs> findFirstByCowIdOrderByDateDesc(String string);

    List<Chaleurs> findByCowId(String cowId);
}
