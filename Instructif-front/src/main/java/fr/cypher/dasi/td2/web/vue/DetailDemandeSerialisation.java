package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Bilan;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Etablissement;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.modele.IntervenantAutre;
import com.samtheo.instructif.metier.modele.IntervenantEnseignant;
import com.samtheo.instructif.metier.modele.IntervenantEtudiant;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class DetailDemandeSerialisation extends Serialisation {

    private static final SimpleDateFormat DATE_FORMAT      = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat DATE_ONLY_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        Demande demande = (Demande) request.getAttribute("demande");
        if (demande == null) {
            response.getWriter().print("null");
            return;
        }

        JsonObjectBuilder root = Json.createObjectBuilder();

        // Shape: { demande, eleve, etablissement, bilan, intervenant }
        // eleve/etablissement are top-level (not nested in demande) so pages that only
        // need student data (demande-prise-en-charge, redaction-bilan) don't have to
        // reach into demande's structure, and detail-demande-bilan can read both.

        // Demande
        JsonObjectBuilder dJson = Json.createObjectBuilder();
        dJson.add("id", demande.getId());
        dJson.add("startDate", demande.getDateHeureDebut() != null ? DATE_FORMAT.format(demande.getDateHeureDebut()) : "");
        dJson.add("endDate", demande.getDateHeureFin() != null ? DATE_FORMAT.format(demande.getDateHeureFin()) : "");
        dJson.add("status", demande.getStatut().name());
        dJson.add("description", demande.getDescription());
        dJson.add("meetingLink", demande.getLienVisio() != null ? demande.getLienVisio() : "");
        dJson.add("subject", demande.getTheme().getMatiere().getNom());
        dJson.add("topic", demande.getTheme().getIntitule());
        root.add("demande", dJson);

        // Eleve complet
        Eleve eleve = demande.getEleve();
        JsonObjectBuilder eleveJson = Json.createObjectBuilder();
        eleveJson.add("lastName",  eleve.getNom());
        eleveJson.add("firstName", eleve.getPrenom());
        eleveJson.add("grade",     eleve.getNiveau());
        eleveJson.add("email",     eleve.getEmail() != null ? eleve.getEmail() : "");
        if (eleve.getDateNaissance() != null) {
            eleveJson.add("birthDate", DATE_ONLY_FORMAT.format(eleve.getDateNaissance()));
        } else {
            eleveJson.addNull("birthDate");
        }
        root.add("eleve", eleveJson);

        // Etablissement
        Etablissement etab = eleve.getEtablissement();
        if (etab != null) {
            JsonObjectBuilder etJson = Json.createObjectBuilder();
            etJson.add("codeUAI",          etab.getCodeUAI()              != null ? etab.getCodeUAI()              : "");
            etJson.add("appellation",      etab.getAppellationOfficielle() != null ? etab.getAppellationOfficielle() : "");
            etJson.add("secteur",          etab.getSecteur()              != null ? etab.getSecteur()              : "");
            etJson.add("adresse",          etab.getAdresse()              != null ? etab.getAdresse()              : "");
            etJson.add("codePostal",       etab.getCodePostal()           != null ? etab.getCodePostal()           : "");
            etJson.add("commune",          etab.getCommune()              != null ? etab.getCommune()              : "");
            etJson.add("latitude",         etab.getLatitude());
            etJson.add("longitude",        etab.getLongitude());
            etJson.add("libelleDepartement", etab.getLibelleDepartement() != null ? etab.getLibelleDepartement()   : "");
            etJson.add("libelleAcademie",  etab.getLibelleAcademie()      != null ? etab.getLibelleAcademie()      : "");
            etJson.add("ips",              etab.getIps());
            root.add("etablissement", etJson);
        } else {
            root.addNull("etablissement");
        }

        // Bilan
        Bilan bilan = demande.getBilan();
        if (bilan != null) {
            JsonObjectBuilder bJson = Json.createObjectBuilder();
            bJson.add("texte", bilan.getTexte() != null ? bilan.getTexte() : "");
            bJson.add("conseils", bilan.getConseils() != null ? bilan.getConseils() : "");
            root.add("bilan", bJson);
        } else {
            root.addNull("bilan");
        }

        // Intervenant
        Intervenant i = demande.getIntervenant();
        if (i != null) {
            JsonObjectBuilder iJson = Json.createObjectBuilder();
            iJson.add("initials", (i.getPrenom().substring(0, 1) + i.getNom().substring(0, 1)).toUpperCase());
            iJson.add("fullName", i.getPrenom() + " " + i.getNom().toUpperCase());
            String typeLabel;
            if (i instanceof IntervenantEtudiant e) {
                typeLabel = "Intervenant étudiant"
                        + (e.getUniversite() != null ? " · " + e.getUniversite() : "")
                        + (e.getSpecialite() != null ? " · " + e.getSpecialite() : "");
            } else if (i instanceof IntervenantEnseignant e) {
                typeLabel = "Enseignant" + (e.getTypeEtablissement() != null ? " · " + e.getTypeEtablissement() : "");
            } else if (i instanceof IntervenantAutre e) {
                typeLabel = e.getActivite() != null ? e.getActivite() : "Intervenant";
            } else {
                typeLabel = "Intervenant";
            }
            iJson.add("typeLabel", typeLabel);
            root.add("intervenant", iJson);
        } else {
            root.add("intervenant", "null");
        }

        response.getWriter().print(root.build().toString());
    }
}
