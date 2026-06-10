/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Eleve;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * @author clemaire
 */
public class RequestSerialisation extends Serialisation {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Demande demande = (Demande)request.getAttribute("request");
        if (demande == null) {
            response.getWriter().print("null");
            return;
        }
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("id", demande.getId());
        jsonObjectBuilder.add("status", demande.getStatut().name());
        jsonObjectBuilder.add("visioLink", demande.getLienVisio());
        jsonObjectBuilder.add("description", demande.getDescription());
        jsonObjectBuilder.add("startDate", demande.getDateHeureDebut() != null ? DATE_FORMAT.format(demande.getDateHeureDebut()) : "");
        jsonObjectBuilder.add("endDate", demande.getDateHeureFin() != null ? DATE_FORMAT.format(demande.getDateHeureFin()) : "");
        jsonObjectBuilder.add("theme", demande.getTheme().getIntitule());
        jsonObjectBuilder.add("subject", demande.getTheme().getMatiere().getNom());
        response.getWriter().print(jsonObjectBuilder.build().toString());
    }
}
