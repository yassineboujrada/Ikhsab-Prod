package com.agri40.management.domain;

import com.agri40.management.domain.enumeration.EventType;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Events.
 */
@Document(collection = "events")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("date")
    private Instant date;

    @Field("cow_id")
    private String cowId;

    @Field("type")
    private EventType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Events id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Events date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getCowId() {
        return this.cowId;
    }

    public Events cowId(String cowId) {
        this.setCowId(cowId);
        return this;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    public EventType getType() {
        return this.type;
    }

    public Events type(EventType type) {
        this.setType(type);
        return this;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Events)) {
            return false;
        }
        return id != null && id.equals(((Events) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Events{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", cowId='" + getCowId() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
