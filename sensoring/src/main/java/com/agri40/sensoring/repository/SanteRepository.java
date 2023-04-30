package com.agri40.sensoring.repository;

import com.agri40.sensoring.domain.Sante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Sante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SanteRepository extends MongoRepository<Sante, String> {}
