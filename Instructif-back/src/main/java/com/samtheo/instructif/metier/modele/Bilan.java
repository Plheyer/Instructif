package com.samtheo.instructif.metier.modele;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bilan")
public class Bilan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String texte;

    @Column(length = 2000)
    private String conseils;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "demande_id", nullable = false, unique = true)
    private Demande demande;

    public Bilan() {
    }

    public Bilan(String texte, String conseils, Demande demande) {
        this.texte = texte;
        this.conseils = conseils;
        this.demande = demande;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public String getConseils() { return conseils; }
    public void setConseils(String conseils) { this.conseils = conseils; }

    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bilan)) return false;
        Bilan bilan = (Bilan) o;
        return Objects.equals(id, bilan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Bilan{id=" + id + ", demande=" + (demande != null ? demande.getId() : null) + "}";
    }
}
