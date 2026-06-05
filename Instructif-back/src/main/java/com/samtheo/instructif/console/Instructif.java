package com.samtheo.instructif.console;

import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.modele.Bilan;
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

public class Instructif {

    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        JpaUtil.creerFabriquePersistance();
        try {
            ServiceEleve serviceEleve = new ServiceEleve();
            ServiceIntervenant serviceIntervenant = new ServiceIntervenant();
            ServiceCatalogue serviceCatalogue = new ServiceCatalogue();
            ServiceDemande serviceDemande = new ServiceDemande();
            ServiceBilan serviceBilan = new ServiceBilan();

            titre("0. Initialisation du catalogue et des intervenants");
            serviceCatalogue.initialiserCatalogue();
            initialiserIntervenantsDemo(serviceIntervenant);

            titre("1. Inscription d'élèves (1 va échouer : email en doublon)");
            testerInscriptionEleves(serviceEleve);

            titre("2. Listing des élèves");
            for (Eleve e : serviceEleve.listerEleves()) {
                System.out.println("   - " + e);
            }

            titre("3. Authentification");
            testerAuthentification(serviceEleve, serviceIntervenant);

            titre("4. Création d'une demande (affectation immédiate) + bilan");
            testerScenarioDemandeBilan(serviceEleve, serviceCatalogue,
                    serviceDemande, serviceBilan);

            titre("5. Cas négatif : aucun intervenant disponible");
            testerAucunIntervenantDispo(serviceEleve, serviceCatalogue,
                    serviceDemande);

            titre("6. Cas limites de validation des services");
            testerCasLimitesValidation(serviceEleve, serviceCatalogue,
                    serviceDemande, serviceBilan);

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerFabriquePersistance();
        }
    }

    private static void testerInscriptionEleves(ServiceEleve service) {
        String uai1 = "0691664J";
        String uai2 = "0440153C";
        String uai3 = "0911185U";

        boolean a = service.inscrireEleve(
                eleve("Martin", "Alice", "2014-09-12", "alice.martin@example.com", "alice123", 5),
                uai1);
        boolean b = service.inscrireEleve(
                eleve("Durand", "Léo", "2013-03-04", "leo.durand@example.com", "leo123", 4),
                uai2);
        boolean c = service.inscrireEleve(
                eleve("Petit", "Inès", "2015-07-18", "ines.petit@example.com", "ines123", 6),
                uai3);
        boolean d = service.inscrireEleve(
                eleve("Bidule", "Alice2", "2014-09-12", "alice.martin@example.com", "x", 5),
                uai1);

        System.out.println("   Inscription 1 (Alice)   : " + a);
        System.out.println("   Inscription 2 (Léo)     : " + b);
        System.out.println("   Inscription 3 (Inès)    : " + c);
        System.out.println("   Inscription 4 (doublon) : " + d + "   (attendu : false)");
    }

    private static void testerAuthentification(ServiceEleve serviceEleve,
            ServiceIntervenant serviceIntervenant) {
        Eleve eleveOk = serviceEleve.authentifierEleve("alice.martin@example.com", "alice123");
        Eleve eleveKO = serviceEleve.authentifierEleve("alice.martin@example.com", "mauvais");
        Intervenant iOk = serviceIntervenant.authentifierIntervenant("camille.s", "camille123");
        Intervenant iKO = serviceIntervenant.authentifierIntervenant("camille.s", "nope");

        System.out.println("   Élève OK        : " + eleveOk);
        System.out.println("   Élève KO        : " + eleveKO + "   (attendu : null)");
        System.out.println("   Intervenant OK  : " + iOk);
        System.out.println("   Intervenant KO  : " + iKO + "   (attendu : null)");
    }

    private static void testerScenarioDemandeBilan(ServiceEleve serviceEleve,
            ServiceCatalogue serviceCatalogue,
            ServiceDemande serviceDemande,
            ServiceBilan serviceBilan) {
        Eleve alice = serviceEleve.authentifierEleve("alice.martin@example.com", "alice123");
        if (alice == null) {
            System.out.println("   /!\\ Alice introuvable, scénario ignoré.");
            return;
        }
        Matiere histGeo = trouverMatiere(serviceCatalogue, "Histoire-Géographie");
        Theme moyenAge = serviceCatalogue.listerThemes(histGeo.getId()).stream()
                .filter(t -> t.getIntitule().contains("féodale"))
                .findFirst().orElse(null);
        if (moyenAge == null) {
            System.out.println("   /!\\ Thème introuvable, scénario ignoré.");
            return;
        }

        // creerDemande affecte directement un intervenant (ou annule la demande).
        Demande demande = serviceDemande.creerDemande(alice.getId(), moyenAge.getId(),
                "Je n'ai pas tout compris sur l'ordre seigneurial, "
                + "et je suis bloquée pour commencer mon devoir.");
        System.out.println("   Demande créée   : " + demande);
        if (demande == null) {
            return;
        }
        System.out.println("   Statut          : " + demande.getStatut());
        System.out.println("   Intervenant     : " + demande.getIntervenant());
        if (demande.getStatut() == Statut.ANNULEE) {
            System.out.println("   Demande annulée (aucun intervenant disponible).");
            return;
        }

        Bilan bilan = serviceBilan.envoyerBilan(demande.getId(),
                "Nous avons revu ensemble la pyramide féodale : suzerain, "
                + "vassal, fief. Alice peut maintenant rédiger son introduction "
                + "en s'appuyant sur ces trois notions.",
                "Relire la leçon p. 42 et faire l'exercice 3. "
                + "Si blocage, reformuler la définition de \"vassal\" avec ses propres mots.");
        System.out.println("   Bilan envoyé    : " + bilan);

        Demande relue = serviceDemande.trouverDemandeParId(demande.getId());
        System.out.println("   Statut final    : " + (relue != null ? relue.getStatut() : "n/a"));
    }

    private static void testerAucunIntervenantDispo(ServiceEleve serviceEleve,
            ServiceCatalogue serviceCatalogue,
            ServiceDemande serviceDemande) {
        // Inès est en 6e ; seuls camille, hoa et lucia couvrent ce niveau
        // (theo = 3..5). En empilant les demandes, ces trois intervenants
        // finissent tous occupés (EN_COURS) et la demande suivante ressort
        // ANNULEE, faute d'intervenant disponible.
        Eleve ines = serviceEleve.authentifierEleve("ines.petit@example.com", "ines123");
        if (ines == null) {
            System.out.println("   /!\\ Inès introuvable, scénario ignoré.");
            return;
        }
        Matiere maths = trouverMatiere(serviceCatalogue, "Mathématiques");
        List<Theme> themes = serviceCatalogue.listerThemes(maths.getId());
        if (themes.isEmpty()) {
            return;
        }
        Long idTheme = themes.get(0).getId();

        boolean annulationObservee = false;
        for (int k = 1; k <= 4 && !annulationObservee; k++) {
            Demande d = serviceDemande.creerDemande(ines.getId(), idTheme,
                    "Demande 6e n°" + k + " : je bloque sur cet exercice.");
            afficherIssueDemande("demande " + k, d);
            annulationObservee = (d != null && d.getStatut() == Statut.ANNULEE);
        }
        System.out.println("   => Annulation faute d'intervenant : "
                + (annulationObservee ? "OUI (attendu)" : "NON (inattendu)"));
    }

    private static void testerCasLimitesValidation(ServiceEleve serviceEleve,
            ServiceCatalogue serviceCatalogue, ServiceDemande serviceDemande,
            ServiceBilan serviceBilan) {
        Eleve alice = serviceEleve.authentifierEleve("alice.martin@example.com", "alice123");
        Matiere hg = trouverMatiere(serviceCatalogue, "Histoire-Géographie");
        List<Theme> themes = (hg != null) ? serviceCatalogue.listerThemes(hg.getId()) : List.of();
        if (alice == null || themes.isEmpty()) {
            System.out.println("   /!\\ Pré-requis manquants, section ignorée.");
            return;
        }
        Long idTheme = themes.get(0).getId();

        Demande vide = serviceDemande.creerDemande(alice.getId(), idTheme, "");
        System.out.println("   Description vide        -> " + vide + "   (attendu : null)");

        Demande tropLongue = serviceDemande.creerDemande(alice.getId(), idTheme, "x".repeat(501));
        System.out.println("   Description > 500 car.  -> " + tropLongue + "   (attendu : null)");

        Demande themeKo = serviceDemande.creerDemande(alice.getId(), 999_999L, "Demande valide.");
        System.out.println("   Thème inexistant        -> " + themeKo + "   (attendu : null)");

        Demande eleveKo = serviceDemande.creerDemande(999_999L, idTheme, "Demande valide.");
        System.out.println("   Élève inexistant        -> " + eleveKo + "   (attendu : null)");

        Bilan bilanKo = serviceBilan.envoyerBilan(999_999L, "Bilan.", "Conseils.");
        System.out.println("   Bilan / demande KO      -> " + bilanKo + "   (attendu : null)");
    }

    private static void afficherIssueDemande(String label, Demande d) {
        if (d == null) {
            System.out.println("   " + label + " : création échouée (null).");
        } else if (d.getStatut() == Statut.ANNULEE) {
            System.out.println("   " + label + " : ANNULEE — aucun intervenant disponible.");
        } else {
            System.out.println("   " + label + " : " + d.getStatut() + " — affectée à "
                    + (d.getIntervenant() != null ? d.getIntervenant().getLogin() : "?"));
        }
    }

    private static void initialiserIntervenantsDemo(ServiceIntervenant service) {
        if (service.authentifierIntervenant("camille.s", "camille123") != null) {
            return;
        }
        service.inscrireIntervenant(new IntervenantEtudiant(
                "Sorel", "Camille", "camille.s", "camille123",
                "+33600000001", 6, 3,
                "Sorbonne", "Langues orientales"));
        service.inscrireIntervenant(new IntervenantEtudiant(
                "Nguyen", "Hoa", "hoa.n", "hoa123",
                "+33600000002", 6, 4,
                "INSA Lyon", "Génie civil"));
        service.inscrireIntervenant(new IntervenantEnseignant(
                "Garcia", "Lucia", "lucia.g", "lucia123",
                "+33600000003", 6, 3,
                "Lycée général"));
        service.inscrireIntervenant(new IntervenantAutre(
                "Roux", "Théo", "theo.r", "theo123",
                "+33600000004", 5, 3,
                "Bénévole associatif"));
    }

    private static Eleve eleve(String nom, String prenom, String iso,
            String email, String mdp, int niveau) {
        Eleve e = new Eleve(nom, prenom, parseDate(iso), email, mdp, niveau);
        return e;
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

    private static Matiere trouverMatiere(ServiceCatalogue service, String nom) {
        return service.listerMatieres().stream()
                .filter(m -> nom.equals(m.getNom()))
                .findFirst().orElse(null);
    }

    private static void titre(String t) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(" " + t);
        System.out.println("==================================================");
    }
}
