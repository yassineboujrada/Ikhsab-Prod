package com.agri40.notification.repository;

import com.agri40.notification.domain.JobRequest;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the JobRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRequestRepository extends MongoRepository<JobRequest, String> {
    List<JobRequest> findAllByProvider(String id);

    List<JobRequest> findAllByConsumer(String id);

    List<JobRequest> findAllByProviderOrConsumer(String provider, String consumer);

    List<JobRequest> findAllByProviderOrConsumerAndServiceStatus(String id, String id2, String string);
}
