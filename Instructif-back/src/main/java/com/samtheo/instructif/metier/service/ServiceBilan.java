package com.samtheo.instructif.metier.service;

import com.samtheo.instructif.dao.BilanDAO;
import com.samtheo.instructif.dao.DemandeDAO;
import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.modele.Bilan;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Statut;
import com.samtheo.instructif.util.Message;
import java.util.Date;

public class ServiceBilan {

    private static final int TEXTE_MIN = 1;
    private static final int TEXTE_MAX = 2000;

    private final BilanDAO bilanDao = new BilanDAO();
    private final DemandeDAO demandeDao = new DemandeDAO();

    /**
     * Persiste le bilan, clôt la demande (TERMINEE), set dateHeureFin,
     * envoie l'email récapitulatif à l'élève. Transaction unique atomique.
     */
    public Bilan envoyerBilan(Long idDemande, String texte, String conseils) {
        if (idDemande == null || texte == null) return null;
        if (texte.length() < TEXTE_MIN || texte.length() > TEXTE_MAX) return null;
        Bilan bilan = null;
        try {
            JpaUtil.creerContextePersistance();
            Demande d = demandeDao.findById(idDemande);
            if (d != null && d.getStatut() == Statut.EN_COURS && d.getIntervenant() != null) {
                JpaUtil.ouvrirTransaction();
                bilan = new Bilan(texte, conseils, d);
                bilanDao.create(bilan);
                d.setBilan(bilan);
                d.setStatut(Statut.TERMINEE);
                d.setDateHeureFin(new Date());
                demandeDao.update(d);
                JpaUtil.validerTransaction();

                String corps = "Bonjour " + d.getEleve().getPrenom() + ",\n\n"
                        + "Voici le bilan de ta séance avec "
                        + d.getIntervenant().getPrenom() + " "
                        + d.getIntervenant().getNom()
                        + " sur le thème « " + d.getTheme().getIntitule() + " » :\n\n"
                        + texte
                        + (conseils != null && !conseils.isBlank()
                                ? "\n\nConseils pour la suite :\n" + conseils
                                : "")
                        + "\n\nBonne continuation,\nL'équipe Instruct'IF";
                Message.envoyerMail("noreply@instructif.fr", d.getEleve().getEmail(),
                        "Bilan de ta séance Instruct'IF", corps);
            }
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            ex.printStackTrace(System.err);
            return null;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return bilan;
    }

    public Bilan trouverBilanParDemande(Long idDemande) {
        if (idDemande == null) return null;
        Bilan b = null;
        try {
            JpaUtil.creerContextePersistance();
            b = bilanDao.findByDemande(idDemande);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return b;
    }
}
