package com.agri40.management.domain;

import java.io.Serializable;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Groups.
 */
@Document(collection = "groups")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("user_id")
    private String userId;

    @Field("gps_adress")
    private Map<String, Object> gpsAdress;

    public String getId() {
        return this.id;
    }

    public Groups id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Groups name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return this.userId;
    }

    public Groups userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Object> getGpsAdress() {
        return this.gpsAdress;
    }

    public Groups gpsAdress(Map<String, Object> gpsAdress) {
        this.setGpsAdress(gpsAdress);
        return this;
    }

    public void setGpsAdress(Map<String, Object> gpsAdress) {
        this.gpsAdress = gpsAdress;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Groups)) {
            return false;
        }
        return id != null && id.equals(((Groups) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Groups{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", userId='" + getUserId() + "'" +
            ", gpsAdress='" + getGpsAdress() + "'" +
            "}";
    }
}
