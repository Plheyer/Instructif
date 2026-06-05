package com.samtheo.instructif.metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "intervenant")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Intervenant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String nom;

    @Column(nullable = false, length = 64)
    private String prenom;

    @Column(nullable = false, unique = true, length = 64)
    private String login;

    @Column(name = "mot_de_passe", nullable = false, length = 128)
    private String motDePasse;

    @Column(length = 32)
    private String telephone;

    @Column(name = "niveau_min", nullable = false)
    private int niveauMin;

    @Column(name = "niveau_max", nullable = false)
    private int niveauMax;

    @OneToMany(mappedBy = "intervenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demande> demandes = new ArrayList<>();

    public Intervenant() {
    }

    public Intervenant(String nom, String prenom, String login, String motDePasse,
                       String telephone, int niveauMin, int niveauMax) {
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.niveauMin = niveauMin;
        this.niveauMax = niveauMax;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String mdp) { this.motDePasse = mdp; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String t) { this.telephone = t; }

    public int getNiveauMin() { return niveauMin; }
    public void setNiveauMin(int n) { this.niveauMin = n; }

    public int getNiveauMax() { return niveauMax; }
    public void setNiveauMax(int n) { this.niveauMax = n; }

    public List<Demande> getDemandes() { return demandes; }
    public void setDemandes(List<Demande> demandes) { this.demandes = demandes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intervenant)) return false;
        Intervenant that = (Intervenant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                ", niveaux=[" + niveauMin + ".." + niveauMax + "]" +
                '}';
    }
}
