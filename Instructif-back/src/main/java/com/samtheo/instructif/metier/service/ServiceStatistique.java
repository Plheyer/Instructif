package com.samtheo.instructif.metier.service;

import com.samtheo.instructif.dao.DemandeDAO;
import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.dto.StatistiquesReseau;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Etablissement;
import com.samtheo.instructif.metier.modele.Statut;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Service de statistiques du réseau (périmètre : tableau de bord). Lecture
 * seule, self-contained (n'appelle aucun autre service) et sans affichage.
 */
public class ServiceStatistique {

    private final DemandeDAO demandeDao = new DemandeDAO();

    /**
     * Calcule les indicateurs globaux à partir des soutiens TERMINEE : nombre
     * de soutiens, durée moyenne (minutes), et répartition des établissements
     * bénéficiaires distincts par académie et par tranche d'IPS. Sans filtre.
     */
    public StatistiquesReseau calculerStatistiques() {
        StatistiquesReseau stats = StatistiquesReseau.vide();
        try {
            JpaUtil.creerContextePersistance();
            List<Demande> soutiens = demandeDao.findByStatut(Statut.TERMINEE);

            long nombreSoutiens = soutiens.size();
            double dureeMoyenne = dureeMoyenneMinutes(soutiens);

            Set<Etablissement> beneficiaires = etablissementsBeneficiaires(soutiens);
            Map<String, Long> parAcademie = repartitionParAcademie(beneficiaires);
            Map<String, Long> parTrancheIps = repartitionParTrancheIps(beneficiaires);
            //Map<String, Long> parCarte = repartitionCarte(beneficiaires);

            stats = new StatistiquesReseau(nombreSoutiens, dureeMoyenne,
                    parAcademie, parTrancheIps);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return StatistiquesReseau.vide();
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return stats;
    }

    private static double dureeMoyenneMinutes(List<Demande> soutiens) {
        long totalMinutes = 0L;
        int comptes = 0;
        for (Demande d : soutiens) {
            if (d.getDateHeureDebut() != null && d.getDateHeureFin() != null) {
                long ms = d.getDateHeureFin().getTime() - d.getDateHeureDebut().getTime();
                if (ms >= 0L) {
                    totalMinutes += ms / 60000L;
                    comptes++;
                }
            }
        }
        return comptes == 0 ? 0.0 : (double) totalMinutes / comptes;
    }
    
    public Set<Etablissement> getEtablissements() {
        Set<Etablissement> etab = Set.of();
        try {
            JpaUtil.creerContextePersistance();
            List<Demande> soutiens = demandeDao.findByStatut(Statut.TERMINEE);
            etab = etablissementsBeneficiaires(soutiens);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return etab;
    }

    private static Set<Etablissement> etablissementsBeneficiaires(List<Demande> soutiens) {
        Set<Etablissement> beneficiaires = new HashSet<>();
        for (Demande d : soutiens) {
            Eleve eleve = d.getEleve();
            if (eleve != null && eleve.getEtablissement() != null) {
                beneficiaires.add(eleve.getEtablissement());
            }
        }
        return beneficiaires;
    }

    private static Map<String, Long> repartitionParAcademie(Set<Etablissement> etablissements) {
        Map<String, Long> parAcademie = new TreeMap<>();
        for (Etablissement e : etablissements) {
            String academie = (e.getLibelleAcademie() != null && !e.getLibelleAcademie().isBlank())
                    ? e.getLibelleAcademie() : "Académie inconnue";
            parAcademie.merge(academie, 1L, Long::sum);
        }
        return parAcademie;
    }

    //private static Map<String, Long> repartitionCarte(Set<Etablissement> etablissements) {
    //    Map<String, Long> stats = new TreeMap<>();
    //    for (Etablissement e : etablissements) {
    //        String key = e.getLatitude() + ";" + e.getLongitude();
    //        stats.merge(key, 1L, Long::sum);
    //    }
    //    return stats;
    //}

    private static Map<String, Long> repartitionParTrancheIps(Set<Etablissement> etablissements) {
        Map<String, Long> parTranche = new LinkedHashMap<>();
        parTranche.put("< 85", 0L);
        parTranche.put("85 – 99", 0L);
        parTranche.put("100 – 114", 0L);
        parTranche.put("115 – 129", 0L);
        parTranche.put("≥ 130", 0L);
        parTranche.put("Non renseigné", 0L);
        for (Etablissement e : etablissements) {
            parTranche.merge(trancheIps(e.getIps()), 1L, Long::sum);
        }
        return parTranche;
    }

    private static String trancheIps(double ips) {
        if (ips <= 0) return "Non renseigné";
        if (ips < 85) return "< 85";
        if (ips < 100) return "85 – 99";
        if (ips < 115) return "100 – 114";
        if (ips < 130) return "115 – 129";
        return "≥ 130";
    }
}
