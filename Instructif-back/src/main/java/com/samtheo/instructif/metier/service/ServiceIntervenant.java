package com.samtheo.instructif.metier.service;

import com.samtheo.instructif.dao.DemandeDAO;
import com.samtheo.instructif.dao.IntervenantDAO;
import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.util.Message;
import java.util.List;

public class ServiceIntervenant {

    private final IntervenantDAO intervenantDao = new IntervenantDAO();
    private final DemandeDAO demandeDao = new DemandeDAO();

    public Boolean inscrireIntervenant(Intervenant intervenant) {
        if (intervenant == null) return Boolean.FALSE;
        Boolean succes = Boolean.FALSE;
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            intervenantDao.create(intervenant);
            JpaUtil.validerTransaction();
            succes = Boolean.TRUE;

            Message.envoyerNotification(intervenant.getTelephone(),
                    "Bienvenue " + intervenant.getPrenom()
                    + " ! Votre compte intervenant Instruct'IF est actif.");
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            ex.printStackTrace(System.err);
            succes = Boolean.FALSE;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return succes;
    }

    public Intervenant authentifierIntervenant(String login, String motDePasse) {
        if (login == null || motDePasse == null) return null;
        Intervenant i = null;
        try {
            JpaUtil.creerContextePersistance();
            i = intervenantDao.authentifier(login, motDePasse);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return i;
    }

    public Intervenant trouverIntervenantParId(Long id) {
        if (id == null) return null;
        Intervenant i = null;
        try {
            JpaUtil.creerContextePersistance();
            i = intervenantDao.findById(id);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return i;
    }

    public List<Demande> listerInterventions(Long idIntervenant) {
        List<Demande> demandes = List.of();
        if (idIntervenant == null) return demandes;
        try {
            JpaUtil.creerContextePersistance();
            demandes = demandeDao.findByIntervenant(idIntervenant);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return demandes;
    }

    /**
     * Retourne l'unique demande au statut EN_COURS affectée à l'intervenant,
     * ou null s'il n'a pas d'affectation en cours. Sert au bloc « Affectation
     * en cours » du menu (interrogé en polling par la couche présentation).
     */
    public Demande trouverAffectationCourante(Long idIntervenant) {
        if (idIntervenant == null) return null;
        Demande affectation = null;
        try {
            JpaUtil.creerContextePersistance();
            affectation = demandeDao.findEnCoursByIntervenant(idIntervenant);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return affectation;
    }
}
