package com.agri40.sensoring.domain;

import java.io.Serializable;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Stream.
 */
@Document(collection = "stream")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Stream implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("type")
    private String type;

    @Field("params")
    private Map<String, Object> params;

    @Field("device_id")
    private String deviceId;

    @Field("created_at")
    private String createdAt;

    @Field("cow_id")
    private String cowId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getCowId() {
        return cowId;
    }

    public Stream cowId(String cowId) {
        this.cowId = cowId;
        return this;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    public String getId() {
        return this.id;
    }

    public Stream id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Stream type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public Stream params(Map<String, Object> params) {
        this.setParams(params);
        return this;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public Stream deviceId(String deviceId) {
        this.setDeviceId(deviceId);
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public Stream createdAt(String createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stream)) {
            return false;
        }
        return id != null && id.equals(((Stream) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        // return a valid jsong string
        return "{" +
            "\"id\":\"" + getId() + "\"" +
            ",\"type\":\"" + getType() + "\"" +
            ",\"params\":\"" + getParams() + "\"" +
            ",\"deviceId\":\"" + getDeviceId() + "\"" +
            ",\"createdAt\":\"" + getCreatedAt() + "\"" +
            ",\"cowId\":\"" + getCowId() + "\"" +
            "}";
    }
}
