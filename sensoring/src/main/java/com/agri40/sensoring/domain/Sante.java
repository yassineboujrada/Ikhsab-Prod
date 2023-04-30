package com.agri40.sensoring.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Sante.
 */
@Document(collection = "sante")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("date")
    private Instant date;

    @Field("duree_position_couchee")
    private String dureePositionCouchee;

    @Field("leve")
    private String leve;

    @Field("pas")
    private String pas;

    @Field("cow_id")
    private String cowId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Sante id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Sante date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDureePositionCouchee() {
        return this.dureePositionCouchee;
    }

    public Sante dureePositionCouchee(String dureePositionCouchee) {
        this.setDureePositionCouchee(dureePositionCouchee);
        return this;
    }

    public void setDureePositionCouchee(String dureePositionCouchee) {
        this.dureePositionCouchee = dureePositionCouchee;
    }

    public String getLeve() {
        return this.leve;
    }

    public Sante leve(String leve) {
        this.setLeve(leve);
        return this;
    }

    public void setLeve(String leve) {
        this.leve = leve;
    }

    public String getPas() {
        return this.pas;
    }

    public Sante pas(String pas) {
        this.setPas(pas);
        return this;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public String getCowId() {
        return this.cowId;
    }

    public Sante cowId(String cowId) {
        this.setCowId(cowId);
        return this;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sante)) {
            return false;
        }
        return id != null && id.equals(((Sante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sante{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", dureePositionCouchee='" + getDureePositionCouchee() + "'" +
            ", leve='" + getLeve() + "'" +
            ", pas='" + getPas() + "'" +
            ", cowId='" + getCowId() + "'" +
            "}";
    }
}
