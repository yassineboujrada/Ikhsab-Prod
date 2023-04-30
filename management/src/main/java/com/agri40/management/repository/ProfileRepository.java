package com.agri40.management.repository;

import com.agri40.management.domain.Profile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Profile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {
    List<Profile> findByAccountType(String accountType);

    List<Profile> findByCity(String city);

    List<Profile> findByCityAndAccountType(String city, String type);

    Optional<Profile> findByUserId(String id);
}
