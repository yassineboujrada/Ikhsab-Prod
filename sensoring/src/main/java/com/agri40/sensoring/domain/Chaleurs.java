package com.agri40.sensoring.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Chaleurs.
 */
@Document(collection = "chaleurs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Chaleurs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("date")
    private Instant date;

    @Field("jrs_lact")
    private String jrsLact;

    @Field("temps")
    private String temps;

    @Field("groupeid")
    private String groupeid;

    @Field("enclosid")
    private String enclosid;

    @Field("activite")
    private String activite;

    @Field("facteur_eleve")
    private String facteurEleve;

    @Field("suspect")
    private String suspect;

    @Field("act_augmentee")
    private String actAugmentee;

    @Field("alarme_chaleur")
    private String alarmeChaleur;

    @Field("pas_de_chaleur")
    private String pasDeChaleur;

    @Field("cow_id")
    private String cowId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Chaleurs id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Chaleurs date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getJrsLact() {
        return this.jrsLact;
    }

    public Chaleurs jrsLact(String jrsLact) {
        this.setJrsLact(jrsLact);
        return this;
    }

    public void setJrsLact(String jrsLact) {
        this.jrsLact = jrsLact;
    }

    public String getTemps() {
        return this.temps;
    }

    public Chaleurs temps(String temps) {
        this.setTemps(temps);
        return this;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public String getGroupeid() {
        return this.groupeid;
    }

    public Chaleurs groupeid(String groupeid) {
        this.setGroupeid(groupeid);
        return this;
    }

    public void setGroupeid(String groupeid) {
        this.groupeid = groupeid;
    }

    public String getEnclosid() {
        return this.enclosid;
    }

    public Chaleurs enclosid(String enclosid) {
        this.setEnclosid(enclosid);
        return this;
    }

    public void setEnclosid(String enclosid) {
        this.enclosid = enclosid;
    }

    public String getActivite() {
        return this.activite;
    }

    public Chaleurs activite(String activite) {
        this.setActivite(activite);
        return this;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getFacteurEleve() {
        return this.facteurEleve;
    }

    public Chaleurs facteurEleve(String facteurEleve) {
        this.setFacteurEleve(facteurEleve);
        return this;
    }

    public void setFacteurEleve(String facteurEleve) {
        this.facteurEleve = facteurEleve;
    }

    public String getSuspect() {
        return this.suspect;
    }

    public Chaleurs suspect(String suspect) {
        this.setSuspect(suspect);
        return this;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public String getActAugmentee() {
        return this.actAugmentee;
    }

    public Chaleurs actAugmentee(String actAugmentee) {
        this.setActAugmentee(actAugmentee);
        return this;
    }

    public void setActAugmentee(String actAugmentee) {
        this.actAugmentee = actAugmentee;
    }

    public String getAlarmeChaleur() {
        return this.alarmeChaleur;
    }

    public Chaleurs alarmeChaleur(String alarmeChaleur) {
        this.setAlarmeChaleur(alarmeChaleur);
        return this;
    }

    public void setAlarmeChaleur(String alarmeChaleur) {
        this.alarmeChaleur = alarmeChaleur;
    }

    public String getPasDeChaleur() {
        return this.pasDeChaleur;
    }

    public Chaleurs pasDeChaleur(String pasDeChaleur) {
        this.setPasDeChaleur(pasDeChaleur);
        return this;
    }

    public void setPasDeChaleur(String pasDeChaleur) {
        this.pasDeChaleur = pasDeChaleur;
    }

    public String getCowId() {
        return this.cowId;
    }

    public Chaleurs cowId(String cowId) {
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
        if (!(o instanceof Chaleurs)) {
            return false;
        }
        return id != null && id.equals(((Chaleurs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chaleurs{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", jrsLact='" + getJrsLact() + "'" +
            ", temps='" + getTemps() + "'" +
            ", groupeid='" + getGroupeid() + "'" +
            ", enclosid='" + getEnclosid() + "'" +
            ", activite='" + getActivite() + "'" +
            ", facteurEleve='" + getFacteurEleve() + "'" +
            ", suspect='" + getSuspect() + "'" +
            ", actAugmentee='" + getActAugmentee() + "'" +
            ", alarmeChaleur='" + getAlarmeChaleur() + "'" +
            ", pasDeChaleur='" + getPasDeChaleur() + "'" +
            ", cowId='" + getCowId() + "'" +
            "}";
    }
}
