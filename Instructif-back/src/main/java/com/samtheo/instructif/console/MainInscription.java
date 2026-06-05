package com.samtheo.instructif.console;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.service.ServiceEleve;

// Main d'inscription : inscrit des élèves (via code), listing et
// authentification. Prérequis : MainInitialisation.
public class MainInscription {

    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        JpaUtil.creerFabriquePersistance("none");
        try {
            ServiceEleve serviceEleve = new ServiceEleve();

            titre("1. Inscription d'élèves (la 4e échoue : email en doublon)");
            boolean a = serviceEleve.inscrireEleve(
                    eleve("Martin", "Alice", "2014-09-12", "alice.martin@example.com", "alice123", 5),
                    "0691664J");
            boolean b = serviceEleve.inscrireEleve(
                    eleve("Durand", "Léo", "2013-03-04", "leo.durand@example.com", "leo123", 4),
                    "0440153C");
            boolean c = serviceEleve.inscrireEleve(
                    eleve("Petit", "Inès", "2015-07-18", "ines.petit@example.com", "ines123", 6),
                    "0911185U");
            boolean d = serviceEleve.inscrireEleve(
                    eleve("Bidule", "Alice2", "2014-09-12", "alice.martin@example.com", "x", 5),
                    "0691664J");

            System.out.println("   Inscription 1 (Alice)   : " + a);
            System.out.println("   Inscription 2 (Léo)     : " + b);
            System.out.println("   Inscription 3 (Inès)    : " + c);
            System.out.println("   Inscription 4 (doublon) : " + d + "   (attendu : false)");

            titre("2. Listing des élèves inscrits");
            for (Eleve e : serviceEleve.listerEleves()) {
                System.out.println("   - " + e);
            }

            titre("3. Authentification");
            Eleve ok = serviceEleve.authentifierEleve("alice.martin@example.com", "alice123");
            Eleve ko = serviceEleve.authentifierEleve("alice.martin@example.com", "mauvais");
            System.out.println("   Élève OK : " + ok);
            System.out.println("   Élève KO : " + ko + "   (attendu : null)");

            System.out.println();
            System.out.println(">> Élèves inscrits. Lancez maintenant MainScenarioEleve.");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerFabriquePersistance();
        }
    }

    private static Eleve eleve(String nom, String prenom, String iso,
            String email, String mdp, int niveau) {
        return new Eleve(nom, prenom, parseDate(iso), email, mdp, niveau);
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
