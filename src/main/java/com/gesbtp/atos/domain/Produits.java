package com.gesbtp.atos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Produits.
 */
@Entity
@Table(name = "produits")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "produits")
public class Produits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 25)
    @Column(name = "nom_produit", nullable = false)
    private String nomProduit;

    @Column(name = "prix_unitaire")
    private Double prixUnitaire;

    @NotNull
    @Size(min = 50)
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public Produits nomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
        return this;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public Produits prixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        return this;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getDescription() {
        return description;
    }

    public Produits description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public Produits user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Produits produits = (Produits) o;
        if (produits.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), produits.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Produits{" +
            "id=" + getId() +
            ", nomProduit='" + getNomProduit() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
