package com.agri40.management.repository;

import com.agri40.management.domain.Enclos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Enclos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnclosRepository extends MongoRepository<Enclos, String> {
    List<Enclos> findAllByGroupId(String groupId);
}
