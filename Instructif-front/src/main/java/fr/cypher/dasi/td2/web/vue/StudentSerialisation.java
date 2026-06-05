/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Eleve;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 *
 * @author clemaire
 */
public class StudentSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Eleve eleve = (Eleve)request.getAttribute("eleve");
        if (eleve == null) {
            response.getWriter().print("null");
            return;
        }
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("id", eleve.getId());
        jsonObjectBuilder.add("email", eleve.getEmail());
        jsonObjectBuilder.add("lastName", eleve.getNom());
        jsonObjectBuilder.add("firstName", eleve.getPrenom());
        jsonObjectBuilder.add("schoolGrade", eleve.getNiveau());
        jsonObjectBuilder.add("date", eleve.getDateNaissance().toString());
        response.getWriter().print(jsonObjectBuilder.build().toString());
    }
}
