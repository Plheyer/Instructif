package com.samtheo.instructif.metier.dto;

import java.util.Map;

/**
 * Indicateurs agrégés du réseau (lecture seule), calculés sur les soutiens
 * au statut TERMINEE. Objet de transfert renvoyé par ServiceStatistique pour
 * alimenter le tableau de bord — ce n'est pas une entité persistée.
 *
 * @param nombreSoutiens             nombre de demandes TERMINEE
 * @param dureeMoyenneMinutes        durée moyenne d'un soutien, en minutes
 * @param etablissementsParAcademie  nb d'établissements bénéficiaires distincts par académie
 * @param etablissementsParTrancheIps nb d'établissements bénéficiaires distincts par tranche d'IPS
 */
public record StatistiquesReseau(
        long nombreSoutiens,
        double dureeMoyenneMinutes,
        Map<String, Long> etablissementsParAcademie,
        Map<String, Long> etablissementsParTrancheIps) {

    /** Statistiques vides (aucun soutien, ou erreur de calcul). */
    public static StatistiquesReseau vide() {
        return new StatistiquesReseau(0L, 0.0, Map.of(), Map.of());
    }
}
