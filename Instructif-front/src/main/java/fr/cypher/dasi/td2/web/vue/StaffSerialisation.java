/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Intervenant;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 *
 * @author clemaire
 */
public class StaffSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Intervenant staff = (Intervenant) request.getAttribute("staff");
        if (staff == null) {
            response.getWriter().print("null");
            return;
        }
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("id", staff.getId());
        jsonObjectBuilder.add("login", staff.getLogin());
        jsonObjectBuilder.add("lastName", staff.getNom());
        jsonObjectBuilder.add("firstName", staff.getPrenom());
        jsonObjectBuilder.add("phone", staff.getTelephone());
        jsonObjectBuilder.add("minSchoolGrade", staff.getNiveauMin());
        jsonObjectBuilder.add("maxSchoolGrade", staff.getNiveauMax());
        response.getWriter().print(jsonObjectBuilder.build().toString());
    }
}
