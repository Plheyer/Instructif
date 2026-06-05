package com.samtheo.instructif.console;

import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.modele.IntervenantAutre;
import com.samtheo.instructif.metier.modele.IntervenantEnseignant;
import com.samtheo.instructif.metier.modele.IntervenantEtudiant;
import com.samtheo.instructif.metier.modele.Matiere;
import com.samtheo.instructif.metier.modele.Statut;
import com.samtheo.instructif.metier.modele.Theme;
import com.samtheo.instructif.metier.service.ServiceBilan;
import com.samtheo.instructif.metier.service.ServiceCatalogue;
import com.samtheo.instructif.metier.service.ServiceDemande;
import com.samtheo.instructif.metier.service.ServiceEleve;
import com.samtheo.instructif.metier.service.ServiceIntervenant;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// Main d'initialisation : réinitialise la base puis charge catalogue et
// intervenants. À exécuter en premier (drop-and-create).
public class MainInitialisation {

    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        JpaUtil.creerFabriquePersistance("drop-and-create");
        try {
            ServiceCatalogue serviceCatalogue = new ServiceCatalogue();
            ServiceIntervenant serviceIntervenant = new ServiceIntervenant();
            ServiceEleve serviceEleve = new ServiceEleve();
            ServiceDemande serviceDemande = new ServiceDemande();
            ServiceBilan serviceBilan = new ServiceBilan();

            titre("1. Réinitialisation du schéma et chargement du catalogue");
            Boolean catalogueOk = serviceCatalogue.initialiserCatalogue();
            System.out.println("   Catalogue initialisé : " + catalogueOk);

            for (Matiere m : serviceCatalogue.listerMatieres()) {
                System.out.println("   - " + m.getNom());
                for (Theme t : serviceCatalogue.listerThemes(m.getId())) {
                    System.out.println("       • " + t.getIntitule());
                }
            }

            titre("2. Inscription des intervenants de démonstration");
            inscrireIntervenants(serviceIntervenant);

            titre("3. Vérification (authentification des intervenants)");
            verifier(serviceIntervenant, "camille.s", "camille123");
            verifier(serviceIntervenant, "hoa.n", "hoa123");
            verifier(serviceIntervenant, "lucia.g", "lucia123");
            verifier(serviceIntervenant, "theo.r", "theo123");

            titre("4. Calibrage des charges (interventions passées)");
            seedHistoriqueInterventions(serviceEleve, serviceCatalogue,
                    serviceDemande, serviceBilan);
            afficherCharge(serviceIntervenant);

            System.out.println();
            System.out.println(">> Base initialisée (camille.s = intervenant prioritaire). "
                    + "Lancez maintenant MainInscription.");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerFabriquePersistance();
        }
    }

    private static void inscrireIntervenants(ServiceIntervenant service) {
        boolean a = service.inscrireIntervenant(new IntervenantEtudiant(
                "Sorel", "Camille", "camille.s", "camille123",
                "+33600000001", 6, 3,
                "Sorbonne", "Langues orientales"));
        boolean b = service.inscrireIntervenant(new IntervenantEtudiant(
                "Nguyen", "Hoa", "hoa.n", "hoa123",
                "+33600000002", 6, 4,
                "INSA Lyon", "Génie civil"));
        boolean c = service.inscrireIntervenant(new IntervenantEnseignant(
                "Garcia", "Lucia", "lucia.g", "lucia123",
                "+33600000003", 6, 3,
                "Lycée général"));
        boolean d = service.inscrireIntervenant(new IntervenantAutre(
                "Roux", "Théo", "theo.r", "theo123",
                "+33600000004", 5, 3,
                "Bénévole associatif"));

        System.out.println("   Étudiant Camille (3..6)   : " + a);
        System.out.println("   Étudiant Hoa (4..6)       : " + b);
        System.out.println("   Enseignant Lucia (3..6)   : " + c);
        System.out.println("   Autre Théo (3..5)         : " + d);
    }

    private static void verifier(ServiceIntervenant service, String login, String mdp) {
        Intervenant i = service.authentifierIntervenant(login, mdp);
        System.out.println("   " + login + " -> " + i);
    }

    // Génère 3 interventions passées pour différencier la charge des intervenants.
    // Effet (équilibrage par nb d'interventions, puis tri nom A→Z) : Garcia,
    // Nguyen et Roux héritent d'une intervention chacun ; Sorel (camille.s) reste
    // à 0 et devient l'intervenant affecté en priorité (affectation déterministe).
    private static void seedHistoriqueInterventions(ServiceEleve serviceEleve,
            ServiceCatalogue serviceCatalogue, ServiceDemande serviceDemande,
            ServiceBilan serviceBilan) {
        boolean inscrit = serviceEleve.inscrireEleve(
                new Eleve("Calibrage", "Demo", parseDate("2014-01-01"),
                        "calibrage.demo@instruct.if", "demo", 5),
                "0691664J");
        if (!inscrit) {
            System.out.println("   /!\\ Élève de calibrage non inscrit (API établissement indispo ?).");
            System.out.println("       Charges laissées à 0 — l'affectation se fera par ordre alphabétique.");
            return;
        }
        Eleve seed = serviceEleve.authentifierEleve("calibrage.demo@instruct.if", "demo");
        Theme theme = premierTheme(serviceCatalogue);
        if (seed == null || theme == null) {
            return;
        }
        int generees = 0;
        for (int k = 1; k <= 3; k++) {
            Demande d = serviceDemande.creerDemande(seed.getId(), theme.getId(),
                    "Intervention de calibrage n°" + k + ".");
            if (d != null && d.getStatut() == Statut.EN_COURS) {
                serviceBilan.envoyerBilan(d.getId(),
                        "Séance de calibrage clôturée.", "Aucun conseil particulier.");
                generees++;
            }
        }
        System.out.println("   " + generees + " intervention(s) passée(s) générée(s).");
    }

    private static void afficherCharge(ServiceIntervenant service) {
        String[][] comptes = {
            {"camille.s", "camille123"},
            {"hoa.n", "hoa123"},
            {"lucia.g", "lucia123"},
            {"theo.r", "theo123"},
        };
        System.out.println("   Charge (nb interventions, le moins chargé affecté en priorité) :");
        for (String[] c : comptes) {
            Intervenant i = service.authentifierIntervenant(c[0], c[1]);
            int n = (i != null) ? service.listerInterventions(i.getId()).size() : -1;
            System.out.println("       " + c[0] + " : " + n);
        }
    }

    private static Theme premierTheme(ServiceCatalogue serviceCatalogue) {
        for (Matiere m : serviceCatalogue.listerMatieres()) {
            List<Theme> themes = serviceCatalogue.listerThemes(m.getId());
            if (!themes.isEmpty()) {
                return themes.get(0);
            }
        }
        return null;
    }

    private static Date parseDate(String iso) {
        try {
            return ISO.parse(iso);
        } catch (Exception ex) {
            Calendar c = Calendar.getInstance();
            c.set(2010, 0, 1);
            return c.getTime();
        }
    }

    private static void titre(String t) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(" " + t);
        System.out.println("==================================================");
    }
}
