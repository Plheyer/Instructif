package com.samtheo.instructif.metier.service;

import com.samtheo.instructif.dao.DemandeDAO;
import com.samtheo.instructif.dao.EleveDAO;
import com.samtheo.instructif.dao.IntervenantDAO;
import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.dao.ThemeDAO;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.modele.Statut;
import com.samtheo.instructif.metier.modele.Theme;
import com.samtheo.instructif.util.Message;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class ServiceDemande {

    private static final int DESCRIPTION_MIN = 1;
    private static final int DESCRIPTION_MAX = 500;

    private final DemandeDAO demandeDao = new DemandeDAO();
    private final EleveDAO eleveDao = new EleveDAO();
    private final ThemeDAO themeDao = new ThemeDAO();
    private final IntervenantDAO intervenantDao = new IntervenantDAO();

    /**
     * Crée une demande de soutien et procède immédiatement à l'affectation
     * (RM-D02). À l'issue du service, la demande est soit EN_COURS — un
     * intervenant disponible et compatible y est affecté —, soit ANNULEE si
     * aucun intervenant n'est disponible. Génère le lien visio. Transaction
     * unique : la recherche et l'affectation se font dans le même contexte.
     */
    public Demande creerDemande(Long idEleve, Long idTheme, String description) {
        if (idEleve == null || idTheme == null || description == null) return null;
        if (description.length() < DESCRIPTION_MIN || description.length() > DESCRIPTION_MAX) {
            return null;
        }
        Demande demande = null;
        Intervenant intervenant = null;
        try {
            JpaUtil.creerContextePersistance();
            Eleve eleve = eleveDao.findById(idEleve);
            Theme theme = themeDao.findById(idTheme);
            if (eleve != null && theme != null) {
                String lienVisio = genererLienVisio(eleve.getId());
                JpaUtil.ouvrirTransaction();
                demande = new Demande(description, Statut.EN_COURS, lienVisio,
                        null, null, eleve, theme);
                theme.addDemande(demande);
                demandeDao.create(demande);

                // Affectation immédiate : on cherche un intervenant disponible et compatible.
                intervenant = chercherIntervenant(eleve.getNiveau());
                if (intervenant != null) {
                    affecterIntervenant(demande, intervenant);  // reste EN_COURS, dateHeureDebut = now()
                } else {
                    demande.setStatut(Statut.ANNULEE);           // aucun intervenant -> annulation auto
                    demande.setDateHeureFin(new Date());
                }
                JpaUtil.validerTransaction();

                // Effet de bord hors transaction : notification de l'intervenant affecté.
                if (intervenant != null) {
                    Message.envoyerNotification(intervenant.getTelephone(),
                            "Nouvelle demande prise en charge — élève "
                            + demande.getEleve().getPrenom() + " " + demande.getEleve().getNom()
                            + " (niveau " + demande.getEleve().getNiveau() + ").\n"
                            + "Thème : " + demande.getTheme().getIntitule() + ".\n"
                            + "Lien visio : " + demande.getLienVisio());
                }
            }
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            ex.printStackTrace(System.err);
            return null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return demande;
    }

    /**
     * Cherche le meilleur intervenant disponible et compatible avec le niveau
     * de l'élève (le moins chargé d'abord), ou null si aucun. Méthode privée
     * SANS transaction : s'exécute dans le contexte de persistance déjà ouvert
     * par creerDemande.
     */
    private Intervenant chercherIntervenant(int niveauEleve) {
        List<Intervenant> candidats = intervenantDao.findDisponiblesPourNiveau(niveauEleve);
        return candidats.isEmpty() ? null : candidats.get(0);
    }

    /**
     * Lie l'intervenant à la demande et fixe l'heure de début. Méthode privée
     * SANS transaction : s'exécute dans la transaction ouverte par creerDemande.
     */
    private void affecterIntervenant(Demande demande, Intervenant intervenant) {
        demande.setIntervenant(intervenant);
        demande.setDateHeureDebut(new Date());
        demandeDao.update(demande);
    }

    public Demande trouverDemandeParId(Long idDemande) {
        if (idDemande == null) return null;
        Demande d = null;
        try {
            JpaUtil.creerContextePersistance();
            d = demandeDao.findById(idDemande);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return d;
    }

    public List<Demande> listerDemandes(Long idEleve) {
        List<Demande> ds = List.of();
        if (idEleve == null) return ds;
        try {
            JpaUtil.creerContextePersistance();
            ds = demandeDao.findByEleve(idEleve);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return ds;
    }

    public List<Demande> listerDemandesRecentes(Long idEleve, int n) {
        List<Demande> ds = List.of();
        if (idEleve == null || n <= 0) return ds;
        try {
            JpaUtil.creerContextePersistance();
            ds = demandeDao.findRecentesByEleve(idEleve, n);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return ds;
    }

    public void annulerDemande(Long idDemande) {
        if (idDemande == null) return;
        try {
            JpaUtil.creerContextePersistance();
            Demande d = demandeDao.findById(idDemande);
            if (d != null) {
                JpaUtil.ouvrirTransaction();
                d.setStatut(Statut.ANNULEE);
                d.setDateHeureFin(new Date());
                demandeDao.update(d);
                JpaUtil.validerTransaction();
            }
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
    }

    private String genererLienVisio(Long idEleve) {
        long token = System.currentTimeMillis();
        return "https://servif.insa-lyon.fr/InteractIF/visio.html"
                + "?eleve=" + URLEncoder.encode(String.valueOf(idEleve), StandardCharsets.UTF_8)
                + "&token=" + token;
    }
}
