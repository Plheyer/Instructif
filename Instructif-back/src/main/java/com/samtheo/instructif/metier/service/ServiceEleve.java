package com.samtheo.instructif.metier.service;

import com.samtheo.instructif.dao.EleveDAO;
import com.samtheo.instructif.dao.EtablissementDAO;
import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Etablissement;
import com.samtheo.instructif.util.ApiEtablissement;
import com.samtheo.instructif.util.Message;
import java.util.List;

public class ServiceEleve {

    private final EleveDAO eleveDao = new EleveDAO();
    private final EtablissementDAO etablissementDao = new EtablissementDAO();

    /**
     * Inscrit l'élève. Récupère son établissement par code UAI : utilise
     * d'abord la BDD, puis l'API data.education.gouv.fr en fallback.
     * Transaction atomique : tout est persisté, ou rien.
     */
    public Boolean inscrireEleve(Eleve eleve, String codeUAI) {
        if (eleve == null || codeUAI == null || codeUAI.isBlank()) {
            return Boolean.FALSE;
        }
        Boolean succes = Boolean.FALSE;
        try {
            JpaUtil.creerContextePersistance();
            Etablissement etablissement = etablissementDao.findByCodeUAI(codeUAI);
            if (etablissement == null) {
                etablissement = ApiEtablissement.recupererParUAI(codeUAI);
            }
            if (etablissement == null) {
                Message.envoyerMail("noreply@instructif.fr", eleve.getEmail(),
                        "Inscription Instruct'IF — échec",
                        "Bonjour " + eleve.getPrenom() + ",\n\n"
                        + "Votre inscription a échoué : le code UAI \""
                        + codeUAI + "\" est introuvable.\n\nL'équipe Instruct'IF");
            } else {
                JpaUtil.ouvrirTransaction();
                if (etablissement.getId() == null) {
                    etablissementDao.create(etablissement);
                }
                etablissement.addEleve(eleve);
                eleveDao.create(eleve);
                JpaUtil.validerTransaction();
                succes = Boolean.TRUE;

                Message.envoyerMail("noreply@instructif.fr", eleve.getEmail(),
                        "Inscription Instruct'IF — bienvenue !",
                        "Bonjour " + eleve.getPrenom() + ",\n\n"
                        + "Votre inscription a été enregistrée avec succès.\n"
                        + "Établissement : " + etablissement.getAppellationOfficielle()
                        + " (" + etablissement.getCommune() + ").\n\n"
                        + "À très vite,\nL'équipe Instruct'IF");
            }
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            ex.printStackTrace(System.err);
            Message.envoyerMail("noreply@instructif.fr", eleve.getEmail(),
                    "Inscription Instruct'IF — échec",
                    "Bonjour " + eleve.getPrenom() + ",\n\n"
                    + "Votre inscription n'a pas pu être enregistrée "
                    + "(email déjà utilisé ?).\n\nL'équipe Instruct'IF");
            return Boolean.FALSE;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return succes;
    }

    public Eleve authentifierEleve(String email, String motDePasse) {
        if (email == null || motDePasse == null) return null;
        Eleve eleve = null;
        try {
            JpaUtil.creerContextePersistance();
            eleve = eleveDao.authentifier(email, motDePasse);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return eleve;
    }

    public Eleve trouverEleveParId(Long id) {
        if (id == null) return null;
        Eleve eleve = null;
        try {
            JpaUtil.creerContextePersistance();
            eleve = eleveDao.findById(id);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return eleve;
    }

    public List<Eleve> listerEleves() {
        List<Eleve> eleves = List.of();
        try {
            JpaUtil.creerContextePersistance();
            eleves = eleveDao.findAll();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return eleves;
    }
}
