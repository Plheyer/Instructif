package com.samtheo.instructif.console;

import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.dto.StatistiquesReseau;
import com.samtheo.instructif.metier.modele.Bilan;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.modele.Matiere;
import com.samtheo.instructif.metier.modele.Statut;
import com.samtheo.instructif.metier.modele.Theme;
import com.samtheo.instructif.metier.service.ServiceBilan;
import com.samtheo.instructif.metier.service.ServiceCatalogue;
import com.samtheo.instructif.metier.service.ServiceDemande;
import com.samtheo.instructif.metier.service.ServiceEleve;
import com.samtheo.instructif.metier.service.ServiceIntervenant;
import com.samtheo.instructif.metier.service.ServiceStatistique;
import java.util.List;
import java.util.Map;

// Main scénario intervenant : authentification, suivi des interventions,
// puis clôture (envoi du bilan) d'une demande en cours. L'affectation est
// désormais réalisée automatiquement par creerDemande.
// Prérequis : MainInscription (et MainScenarioEleve pour une demande à traiter).
public class MainScenarioIntervenant {

    public static void main(String[] args) {
        JpaUtil.creerFabriquePersistance("none");
        try {
            ServiceEleve serviceEleve = new ServiceEleve();
            ServiceIntervenant serviceIntervenant = new ServiceIntervenant();
            ServiceCatalogue serviceCatalogue = new ServiceCatalogue();
            ServiceDemande serviceDemande = new ServiceDemande();
            ServiceBilan serviceBilan = new ServiceBilan();
            ServiceStatistique serviceStatistique = new ServiceStatistique();

            titre("1. Authentification de l'intervenant");
            Intervenant camille = serviceIntervenant.authentifierIntervenant("camille.s", "camille123");
            System.out.println("   Connecté : " + camille);
            if (camille == null) {
                System.out.println("   /!\\ Intervenant introuvable. Lancez d'abord MainInitialisation.");
                return;
            }

            titre("2. Mes interventions et mon affectation courante");
            List<Demande> interventions = serviceIntervenant.listerInterventions(camille.getId());
            System.out.println("   Interventions passées/en cours : " + interventions.size());
            for (Demande d : interventions) {
                System.out.println("   - " + d);
            }
            Demande courante = serviceIntervenant.trouverAffectationCourante(camille.getId());
            System.out.println("   Affectation courante : "
                    + (courante != null ? courante : "aucune"));

            titre("3. Obtention d'une demande en cours (affectation automatique)");
            Demande demande = recupererDemandeACloturer(
                    serviceEleve, serviceCatalogue, serviceDemande);
            if (demande == null) {
                System.out.println("   /!\\ Impossible d'obtenir une demande. "
                        + "Lancez MainInscription et MainScenarioEleve.");
                return;
            }
            if (demande.getStatut() != Statut.EN_COURS || demande.getIntervenant() == null) {
                System.out.println("   Demande non exploitable (statut " + demande.getStatut()
                        + ") — aucun intervenant n'était disponible à la création.");
                return;
            }
            Intervenant affecte = demande.getIntervenant();
            System.out.println("   Demande à traiter          : " + demande);
            System.out.println("   Affectée automatiquement à : " + affecte.getLogin());
            if (!affecte.getLogin().equals(camille.getLogin())) {
                System.out.println("   /!\\ Demande affectée à " + affecte.getLogin()
                        + ", pas à l'intervenant connecté (" + camille.getLogin()
                        + "). Vérifiez le calibrage des charges (MainInitialisation).");
            }
            System.out.println("   Affectation courante de " + affecte.getLogin() + " : "
                    + serviceIntervenant.trouverAffectationCourante(affecte.getId()));

            titre("4. Rédaction et envoi du bilan");
            Bilan bilan = serviceBilan.envoyerBilan(demande.getId(),
                    "Nous avons revu ensemble la pyramide féodale : suzerain, "
                    + "vassal, fief. L'élève peut maintenant rédiger son introduction "
                    + "en s'appuyant sur ces trois notions.",
                    "Relire la leçon p. 42 et faire l'exercice 3. "
                    + "Si blocage, reformuler la définition de « vassal ».");
            System.out.println("   Bilan envoyé : " + bilan);

            Demande relue = serviceDemande.trouverDemandeParId(demande.getId());
            System.out.println("   Statut final : " + (relue != null ? relue.getStatut() : "n/a"));
            System.out.println("   Interventions de " + affecte.getLogin() + " : "
                    + serviceIntervenant.listerInterventions(affecte.getId()).size());

            titre("5. Consultation du tableau de bord (clic sur « Statistiques »)");
            afficherStatistiques(serviceStatistique.calculerStatistiques());

            System.out.println();
            System.out.println(">> Séance clôturée. L'élève peut relire son bilan via MainScenarioEleve.");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerFabriquePersistance();
        }
    }

    // Reprend la dernière demande EN_COURS d'Alice, ou en crée une sinon
    // (creerDemande tente l'affectation immédiatement à la création).
    private static Demande recupererDemandeACloturer(ServiceEleve serviceEleve,
            ServiceCatalogue serviceCatalogue, ServiceDemande serviceDemande) {
        Eleve alice = serviceEleve.authentifierEleve("alice.martin@example.com", "alice123");
        if (alice == null) {
            return null;
        }
        for (Demande d : serviceDemande.listerDemandesRecentes(alice.getId(), 10)) {
            if (d.getStatut() == Statut.EN_COURS) {
                return d;
            }
        }
        Theme theme = trouverThemeFeodal(serviceCatalogue);
        if (theme == null) {
            return null;
        }
        return serviceDemande.creerDemande(alice.getId(), theme.getId(),
                "Je suis bloquée sur l'ordre seigneurial pour mon devoir.");
    }

    private static Theme trouverThemeFeodal(ServiceCatalogue serviceCatalogue) {
        for (Matiere m : serviceCatalogue.listerMatieres()) {
            if ("Histoire-Géographie".equals(m.getNom())) {
                List<Theme> themes = serviceCatalogue.listerThemes(m.getId());
                return themes.stream()
                        .filter(t -> t.getIntitule().contains("féodale"))
                        .findFirst()
                        .orElse(themes.isEmpty() ? null : themes.get(0));
            }
        }
        return null;
    }

    private static void afficherStatistiques(StatistiquesReseau stats) {
        System.out.println("   Nombre de soutiens : " + stats.nombreSoutiens());
        System.out.printf("   Durée moyenne      : %.1f min%n", stats.dureeMoyenneMinutes());
        System.out.println("   Établissements bénéficiaires par académie :");
        if (stats.etablissementsParAcademie().isEmpty()) {
            System.out.println("       (aucun)");
        } else {
            for (Map.Entry<String, Long> e : stats.etablissementsParAcademie().entrySet()) {
                System.out.println("       " + e.getKey() + " : " + e.getValue());
            }
        }
        System.out.println("   Établissements bénéficiaires par tranche d'IPS :");
        for (Map.Entry<String, Long> e : stats.etablissementsParTrancheIps().entrySet()) {
            System.out.println("       " + e.getKey() + " : " + e.getValue());
        }
    }

    private static void titre(String t) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(" " + t);
        System.out.println("==================================================");
    }
}
