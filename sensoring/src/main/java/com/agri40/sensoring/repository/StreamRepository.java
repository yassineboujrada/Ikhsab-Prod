package com.agri40.sensoring.repository;

import com.agri40.sensoring.domain.Stream;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Stream entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StreamRepository extends MongoRepository<Stream, String> {
    List<Stream> findByCowIdAndTypeOrderByCreatedAtDesc(String string, String string2);

    Optional<Stream> findFirstByOrderByCreatedAtDesc();

    // findByCowId
    List<Stream> findByCowId(String cowId);
}
