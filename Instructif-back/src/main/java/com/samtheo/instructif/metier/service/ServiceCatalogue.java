package com.samtheo.instructif.metier.service;

import com.samtheo.instructif.dao.JpaUtil;
import com.samtheo.instructif.dao.MatiereDAO;
import com.samtheo.instructif.dao.ThemeDAO;
import com.samtheo.instructif.metier.modele.Matiere;
import com.samtheo.instructif.metier.modele.Theme;
import java.util.List;

public class ServiceCatalogue {

    private final MatiereDAO matiereDao = new MatiereDAO();
    private final ThemeDAO themeDao = new ThemeDAO();

    public List<Matiere> listerMatieres() {
        List<Matiere> matieres = List.of();
        try {
            JpaUtil.creerContextePersistance();
            matieres = matiereDao.findAll();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return matieres;
    }

    public List<Theme> listerThemes(Long idMatiere) {
        List<Theme> themes = List.of();
        if (idMatiere == null) return themes;
        try {
            JpaUtil.creerContextePersistance();
            themes = themeDao.findByMatiere(idMatiere);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return themes;
    }

    /**
     * Initialisation du catalogue (matières + thèmes de base) et premières
     * données de démonstration. À exécuter une seule fois.
     */
    public Boolean initialiserCatalogue() {
        Boolean succes = Boolean.FALSE;
        try {
            JpaUtil.creerContextePersistance();
            if (matiereDao.findAll().isEmpty()) {
                JpaUtil.ouvrirTransaction();

                String[][] catalogue = {
                    { "Mathématiques",      "Fractions", "Géométrie", "Théorème de Pythagore", "Équations" },
                    { "Français",           "Conjugaison", "Grammaire", "Analyse de texte" },
                    { "Histoire-Géographie","Moyen-Âge — société féodale", "Révolution française", "Climats du monde" },
                    { "Sciences",           "Système solaire", "Le corps humain", "L'énergie" },
                    { "Anglais",            "Présent simple", "Past simple", "Vocabulaire de la maison" },
                    { "Technologie",        "Algorithmique", "Schémas électriques" }
                };
                for (String[] ligne : catalogue) {
                    Matiere m = new Matiere(ligne[0]);
                    matiereDao.create(m);
                    for (int k = 1; k < ligne.length; k++) {
                        themeDao.create(new Theme(ligne[k], m));
                    }
                }
                JpaUtil.validerTransaction();
            }
            succes = Boolean.TRUE;
        } catch (Exception ex) {
            JpaUtil.annulerTransaction();
            ex.printStackTrace(System.err);
            return Boolean.FALSE;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return succes;
    }
}
