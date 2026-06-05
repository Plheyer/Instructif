package com.samtheo.instructif.metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "etablissement")
public class Etablissement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_uai", nullable = false, unique = true, length = 16)
    private String codeUAI;

    @Column(name = "appellation_officielle", length = 255)
    private String appellationOfficielle;

    @Column(length = 64)
    private String secteur;

    @Column(length = 255)
    private String adresse;

    @Column(name = "code_postal", length = 16)
    private String codePostal;

    @Column(length = 128)
    private String commune;

    private double latitude;

    private double longitude;

    @Column(name = "libelle_departement", length = 128)
    private String libelleDepartement;

    @Column(name = "libelle_academie", length = 128)
    private String libelleAcademie;

    private double ips;

    @OneToMany(mappedBy = "etablissement", fetch = FetchType.LAZY)
    private List<Eleve> eleves = new ArrayList<>();

    public Etablissement() {
    }

    public Etablissement(String codeUAI, String appellationOfficielle, String secteur,
                         String adresse, String codePostal, String commune,
                         double latitude, double longitude,
                         String libelleDepartement, String libelleAcademie, double ips) {
        this.codeUAI = codeUAI;
        this.appellationOfficielle = appellationOfficielle;
        this.secteur = secteur;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.commune = commune;
        this.latitude = latitude;
        this.longitude = longitude;
        this.libelleDepartement = libelleDepartement;
        this.libelleAcademie = libelleAcademie;
        this.ips = ips;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodeUAI() { return codeUAI; }
    public void setCodeUAI(String codeUAI) { this.codeUAI = codeUAI; }

    public String getAppellationOfficielle() { return appellationOfficielle; }
    public void setAppellationOfficielle(String s) { this.appellationOfficielle = s; }

    public String getSecteur() { return secteur; }
    public void setSecteur(String secteur) { this.secteur = secteur; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getCommune() { return commune; }
    public void setCommune(String commune) { this.commune = commune; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getLibelleDepartement() { return libelleDepartement; }
    public void setLibelleDepartement(String s) { this.libelleDepartement = s; }

    public String getLibelleAcademie() { return libelleAcademie; }
    public void setLibelleAcademie(String s) { this.libelleAcademie = s; }

    public double getIps() { return ips; }
    public void setIps(double ips) { this.ips = ips; }

    public List<Eleve> getEleves() { return eleves; }
    public void setEleves(List<Eleve> eleves) { this.eleves = eleves; }

    public void addEleve(Eleve eleve) {
        eleves.add(eleve);
        eleve.setEtablissement(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Etablissement)) return false;
        Etablissement that = (Etablissement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Etablissement{" +
                "id=" + id +
                ", codeUAI='" + codeUAI + '\'' +
                ", appellation='" + appellationOfficielle + '\'' +
                ", commune='" + commune + '\'' +
                ", ips=" + ips +
                '}';
    }
}
