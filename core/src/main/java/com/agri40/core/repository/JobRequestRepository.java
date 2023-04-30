package com.agri40.core.repository;

import com.agri40.core.domain.JobRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the JobRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRequestRepository extends ReactiveMongoRepository<JobRequest, String> {
    Flux<JobRequest> findAllBy(Pageable pageable);
}
