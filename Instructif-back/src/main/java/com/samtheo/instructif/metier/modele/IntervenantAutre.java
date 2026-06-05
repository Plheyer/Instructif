package com.samtheo.instructif.metier.modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "intervenant_autre")
public class IntervenantAutre extends Intervenant {

    @Column(length = 128)
    private String activite;

    public IntervenantAutre() {
        super();
    }

    public IntervenantAutre(String nom, String prenom, String login, String motDePasse,
                            String telephone, int niveauMin, int niveauMax,
                            String activite) {
        super(nom, prenom, login, motDePasse, telephone, niveauMin, niveauMax);
        this.activite = activite;
    }

    public String getActivite() { return activite; }
    public void setActivite(String activite) { this.activite = activite; }
}
