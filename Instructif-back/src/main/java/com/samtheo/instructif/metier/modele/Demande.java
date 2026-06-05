package com.samtheo.instructif.metier.modele;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "demande")
public class Demande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Statut statut;

    @Column(name = "lien_visio", length = 512)
    private String lienVisio;

    @Column(name = "date_heure_debut")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHeureDebut;

    @Column(name = "date_heure_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHeureFin;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "eleve_id", nullable = false)
    private Eleve eleve;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intervenant_id")
    private Intervenant intervenant;

    @OneToOne(mappedBy = "demande", cascade = CascadeType.ALL, fetch = FetchType.LAZY,
              orphanRemoval = true)
    private Bilan bilan;

    public Demande() {
    }

    public Demande(String description, Statut statut, String lienVisio,
                   Date dateHeureDebut, Date dateHeureFin,
                   Eleve eleve, Theme theme) {
        this.description = description;
        this.statut = statut;
        this.lienVisio = lienVisio;
        this.dateHeureDebut = dateHeureDebut;
        this.dateHeureFin = dateHeureFin;
        this.eleve = eleve;
        this.theme = theme;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public String getLienVisio() { return lienVisio; }
    public void setLienVisio(String lienVisio) { this.lienVisio = lienVisio; }

    public Date getDateHeureDebut() { return dateHeureDebut; }
    public void setDateHeureDebut(Date d) { this.dateHeureDebut = d; }

    public Date getDateHeureFin() { return dateHeureFin; }
    public void setDateHeureFin(Date d) { this.dateHeureFin = d; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Theme getTheme() { return theme; }
    public void setTheme(Theme theme) { this.theme = theme; }

    public Intervenant getIntervenant() { return intervenant; }
    public void setIntervenant(Intervenant i) { this.intervenant = i; }

    public Bilan getBilan() { return bilan; }
    public void setBilan(Bilan bilan) { this.bilan = bilan; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Demande)) return false;
        Demande demande = (Demande) o;
        return Objects.equals(id, demande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Demande{" +
                "id=" + id +
                ", statut=" + statut +
                ", eleve=" + (eleve != null ? eleve.getId() : null) +
                ", theme=" + (theme != null ? theme.getIntitule() : null) +
                ", intervenant=" + (intervenant != null ? intervenant.getLogin() : "—") +
                '}';
    }
}
