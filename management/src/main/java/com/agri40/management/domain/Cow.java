package com.agri40.management.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Cow.
 */
@Document(collection = "cow")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("numero")
    private String numero;

    @Field("groupe_id")
    private String groupeId;

    @Field("enclos_id")
    private String enclosId;

    @Field("repondeur")
    private String repondeur;

    @Field("nom")
    private String nom;

    @Field("user_id")
    private String userId;

    @Field("waiting_for_inseminator")
    private Boolean waitingForInseminator;

    @Field("rfid")
    private String rfid;

    @Field("pedometre")
    private String pedometre;

    @Field("collar")
    private String collar;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Cow id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Cow numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getGroupeId() {
        return this.groupeId;
    }

    public Cow groupeId(String groupeId) {
        this.setGroupeId(groupeId);
        return this;
    }

    public void setGroupeId(String groupeId) {
        this.groupeId = groupeId;
    }

    public String getEnclosId() {
        return this.enclosId;
    }

    public Cow enclosId(String enclosId) {
        this.setEnclosId(enclosId);
        return this;
    }

    public void setEnclosId(String enclosId) {
        this.enclosId = enclosId;
    }

    public String getRepondeur() {
        return this.repondeur;
    }

    public Cow repondeur(String repondeur) {
        this.setRepondeur(repondeur);
        return this;
    }

    public void setRepondeur(String repondeur) {
        this.repondeur = repondeur;
    }

    public String getNom() {
        return this.nom;
    }

    public Cow nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUserId() {
        return this.userId;
    }

    public Cow userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getWaitingForInseminator() {
        return this.waitingForInseminator;
    }

    public Cow waitingForInseminator(Boolean waitingForInseminator) {
        this.setWaitingForInseminator(waitingForInseminator);
        return this;
    }

    public void setWaitingForInseminator(Boolean waitingForInseminator) {
        this.waitingForInseminator = waitingForInseminator;
    }

    public String getRfid() {
        return this.rfid;
    }

    public Cow rfid(String rfid) {
        this.setRfid(rfid);
        return this;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getPedometre() {
        return this.pedometre;
    }

    public Cow pedometre(String pedometre) {
        this.setPedometre(pedometre);
        return this;
    }

    public void setPedometre(String pedometre) {
        this.pedometre = pedometre;
    }

    public String getCollar() {
        return this.collar;
    }

    public Cow collar(String collar) {
        this.setCollar(collar);
        return this;
    }

    public void setCollar(String collar) {
        this.collar = collar;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cow)) {
            return false;
        }
        return id != null && id.equals(((Cow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cow{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", groupeId='" + getGroupeId() + "'" +
            ", enclosId='" + getEnclosId() + "'" +
            ", repondeur='" + getRepondeur() + "'" +
            ", nom='" + getNom() + "'" +
            ", userId='" + getUserId() + "'" +
            ", waitingForInseminator='" + getWaitingForInseminator() + "'" +
            ", rfid='" + getRfid() + "'" +
            ", pedometre='" + getPedometre() + "'" +
            ", collar='" + getCollar() + "'" +
            "}";
    }
}
