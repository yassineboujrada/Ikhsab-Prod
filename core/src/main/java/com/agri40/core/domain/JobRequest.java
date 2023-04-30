package com.agri40.core.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A JobRequest.
 */
@Document(collection = "job_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("consumer")
    private String consumer;

    @Field("provider")
    private String provider;

    @Field("service_status")
    private String serviceStatus;

    @Field("room_id")
    private String roomId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public JobRequest id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumer() {
        return this.consumer;
    }

    public JobRequest consumer(String consumer) {
        this.setConsumer(consumer);
        return this;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getProvider() {
        return this.provider;
    }

    public JobRequest provider(String provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getServiceStatus() {
        return this.serviceStatus;
    }

    public JobRequest serviceStatus(String serviceStatus) {
        this.setServiceStatus(serviceStatus);
        return this;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public JobRequest roomId(String roomId) {
        this.setRoomId(roomId);
        return this;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobRequest)) {
            return false;
        }
        return id != null && id.equals(((JobRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobRequest{" +
            "id=" + getId() +
            ", consumer='" + getConsumer() + "'" +
            ", provider='" + getProvider() + "'" +
            ", serviceStatus='" + getServiceStatus() + "'" +
            ", roomId='" + getRoomId() + "'" +
            "}";
    }
}
