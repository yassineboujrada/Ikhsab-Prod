package com.agri40.management.repository;

import com.agri40.management.domain.Groups;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Groups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsRepository extends MongoRepository<Groups, String> {
    List<Groups> findByUserId(String userId);
}
