package com.samtheo.instructif.console;

import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.modele.Bilan;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Matiere;
import com.samtheo.instructif.metier.modele.Statut;
import com.samtheo.instructif.metier.modele.Theme;
import com.samtheo.instructif.metier.service.ServiceBilan;
import com.samtheo.instructif.metier.service.ServiceCatalogue;
import com.samtheo.instructif.metier.service.ServiceDemande;
import com.samtheo.instructif.metier.service.ServiceEleve;
import java.util.List;

// Main scénario élève : authentification, navigation catalogue, dépôt d'une
// demande, suivi et lecture du bilan. Prérequis : MainInscription.
public class MainScenarioEleve {

    public static void main(String[] args) {
        JpaUtil.creerFabriquePersistance("none");
        try {
            ServiceEleve serviceEleve = new ServiceEleve();
            ServiceCatalogue serviceCatalogue = new ServiceCatalogue();
            ServiceDemande serviceDemande = new ServiceDemande();
            ServiceBilan serviceBilan = new ServiceBilan();

            titre("1. Authentification de l'élève");
            Eleve alice = serviceEleve.authentifierEleve("alice.martin@example.com", "alice123");
            System.out.println("   Connecté : " + alice);
            if (alice == null) {
                System.out.println("   /!\\ Élève introuvable. Lancez d'abord MainInscription.");
                return;
            }
            System.out.println("   Relecture par id : "
                    + serviceEleve.trouverEleveParId(alice.getId()));

            titre("2. Navigation dans le catalogue");
            Theme themeChoisi = null;
            for (Matiere m : serviceCatalogue.listerMatieres()) {
                System.out.println("   " + m.getNom());
                List<Theme> themes = serviceCatalogue.listerThemes(m.getId());
                for (Theme t : themes) {
                    System.out.println("       • " + t.getIntitule());
                }
                if (themeChoisi == null && "Histoire-Géographie".equals(m.getNom())) {
                    themeChoisi = themes.stream()
                            .filter(t -> t.getIntitule().contains("féodale"))
                            .findFirst().orElse(themes.isEmpty() ? null : themes.get(0));
                }
            }
            if (themeChoisi == null) {
                System.out.println("   /!\\ Catalogue vide. Lancez d'abord MainInitialisation.");
                return;
            }
            System.out.println("   Thème retenu : " + themeChoisi.getIntitule());

            titre("3. Dépôt d'une demande de soutien");
            Demande demande = serviceDemande.creerDemande(alice.getId(), themeChoisi.getId(),
                    "Je n'ai pas tout compris sur l'ordre seigneurial, "
                    + "et je suis bloquée pour commencer mon devoir.");
            System.out.println("   Demande créée : " + demande);
            if (demande != null) {
                System.out.println("   Lien visio    : " + demande.getLienVisio());
                System.out.println("   Statut        : " + demande.getStatut());
                if (demande.getStatut() == Statut.EN_COURS && demande.getIntervenant() != null) {
                    System.out.println("   Intervenant   : " + demande.getIntervenant().getLogin()
                            + " (affecté automatiquement à la création)");
                } else if (demande.getStatut() == Statut.ANNULEE) {
                    System.out.println("   (Aucun intervenant disponible — demande annulée.)");
                }
            }

            titre("4. Suivi de mes demandes");
            List<Demande> toutes = serviceDemande.listerDemandes(alice.getId());
            System.out.println("   Total : " + toutes.size() + " demande(s)");
            for (Demande d : serviceDemande.listerDemandesRecentes(alice.getId(), 5)) {
                System.out.println("   - " + d);
                if (d.getStatut() == Statut.TERMINEE) {
                    Bilan bilan = serviceBilan.trouverBilanParDemande(d.getId());
                    afficherBilan(bilan);
                }
            }

            System.out.println();
            System.out.println(">> Demande déposée. Lancez maintenant MainScenarioIntervenant "
                    + "pour la prise en charge, puis relancez ce main pour lire le bilan.");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerFabriquePersistance();
        }
    }

    private static void afficherBilan(Bilan bilan) {
        if (bilan == null) {
            System.out.println("       (aucun bilan)");
            return;
        }
        System.out.println("       Bilan   : " + bilan.getTexte());
        if (bilan.getConseils() != null && !bilan.getConseils().isBlank()) {
            System.out.println("       Conseils: " + bilan.getConseils());
        }
    }

    private static void titre(String t) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(" " + t);
        System.out.println("==================================================");
    }
}
