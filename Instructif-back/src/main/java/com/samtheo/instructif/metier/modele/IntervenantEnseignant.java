package com.samtheo.instructif.metier.modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "intervenant_enseignant")
public class IntervenantEnseignant extends Intervenant {

    @Column(name = "type_etablissement", length = 128)
    private String typeEtablissement;

    public IntervenantEnseignant() {
        super();
    }

    public IntervenantEnseignant(String nom, String prenom, String login, String motDePasse,
                                 String telephone, int niveauMin, int niveauMax,
                                 String typeEtablissement) {
        super(nom, prenom, login, motDePasse, telephone, niveauMin, niveauMax);
        this.typeEtablissement = typeEtablissement;
    }

    public String getTypeEtablissement() { return typeEtablissement; }
    public void setTypeEtablissement(String t) { this.typeEtablissement = t; }
}
