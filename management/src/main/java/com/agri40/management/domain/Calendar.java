package com.agri40.management.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Calendar.
 */
@Document(collection = "calendar")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("lactation")
    private String lactation;

    @Field("jrs_lact")
    private String jrsLact;

    @Field("statut_reproduction")
    private String statutReproduction;

    @Field("etat_prod")
    private String etatProd;

    @Field("date_naissance")
    private Instant dateNaissance;

    @Field("velage")
    private Instant velage;

    @Field("chaleur")
    private Instant chaleur;

    @Field("insemination")
    private Instant insemination;

    @Field("cow_id")
    private String cowId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Calendar id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLactation() {
        return this.lactation;
    }

    public Calendar lactation(String lactation) {
        this.setLactation(lactation);
        return this;
    }

    public void setLactation(String lactation) {
        this.lactation = lactation;
    }

    public String getJrsLact() {
        return this.jrsLact;
    }

    public Calendar jrsLact(String jrsLact) {
        this.setJrsLact(jrsLact);
        return this;
    }

    public void setJrsLact(String jrsLact) {
        this.jrsLact = jrsLact;
    }

    public String getStatutReproduction() {
        return this.statutReproduction;
    }

    public Calendar statutReproduction(String statutReproduction) {
        this.setStatutReproduction(statutReproduction);
        return this;
    }

    public void setStatutReproduction(String statutReproduction) {
        this.statutReproduction = statutReproduction;
    }

    public String getEtatProd() {
        return this.etatProd;
    }

    public Calendar etatProd(String etatProd) {
        this.setEtatProd(etatProd);
        return this;
    }

    public void setEtatProd(String etatProd) {
        this.etatProd = etatProd;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public Calendar dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Instant getVelage() {
        return this.velage;
    }

    public Calendar velage(Instant velage) {
        this.setVelage(velage);
        return this;
    }

    public void setVelage(Instant velage) {
        this.velage = velage;
    }

    public Instant getChaleur() {
        return this.chaleur;
    }

    public Calendar chaleur(Instant chaleur) {
        this.setChaleur(chaleur);
        return this;
    }

    public void setChaleur(Instant chaleur) {
        this.chaleur = chaleur;
    }

    public Instant getInsemination() {
        return this.insemination;
    }

    public Calendar insemination(Instant insemination) {
        this.setInsemination(insemination);
        return this;
    }

    public void setInsemination(Instant insemination) {
        this.insemination = insemination;
    }

    public String getCowId() {
        return this.cowId;
    }

    public Calendar cowId(String cowId) {
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
        if (!(o instanceof Calendar)) {
            return false;
        }
        return id != null && id.equals(((Calendar) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Calendar{" +
            "id=" + getId() +
            ", lactation='" + getLactation() + "'" +
            ", jrsLact='" + getJrsLact() + "'" +
            ", statutReproduction='" + getStatutReproduction() + "'" +
            ", etatProd='" + getEtatProd() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", velage='" + getVelage() + "'" +
            ", chaleur='" + getChaleur() + "'" +
            ", insemination='" + getInsemination() + "'" +
            ", cowId='" + getCowId() + "'" +
            "}";
    }
}
