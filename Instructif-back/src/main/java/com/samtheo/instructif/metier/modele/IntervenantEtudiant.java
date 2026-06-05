package com.samtheo.instructif.metier.modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "intervenant_etudiant")
public class IntervenantEtudiant extends Intervenant {

    @Column(length = 128)
    private String universite;

    @Column(length = 128)
    private String specialite;

    public IntervenantEtudiant() {
        super();
    }

    public IntervenantEtudiant(String nom, String prenom, String login, String motDePasse,
                               String telephone, int niveauMin, int niveauMax,
                               String universite, String specialite) {
        super(nom, prenom, login, motDePasse, telephone, niveauMin, niveauMax);
        this.universite = universite;
        this.specialite = specialite;
    }

    public String getUniversite() { return universite; }
    public void setUniversite(String universite) { this.universite = universite; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
}
