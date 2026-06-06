/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.modele.IntervenantAutre;
import com.samtheo.instructif.metier.modele.IntervenantEnseignant;
import com.samtheo.instructif.metier.modele.IntervenantEtudiant;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
        jsonObjectBuilder.add("phone", staff.getTelephone() != null ? staff.getTelephone() : "");
        jsonObjectBuilder.add("minSchoolGrade", staff.getNiveauMin());
        jsonObjectBuilder.add("maxSchoolGrade", staff.getNiveauMax());

        if (staff instanceof IntervenantEtudiant etudiant) {
            jsonObjectBuilder.add("type", "STUDENT");
            jsonObjectBuilder.add("university", etudiant.getUniversite() != null ? etudiant.getUniversite() : "");
            jsonObjectBuilder.add("speciality", etudiant.getSpecialite() != null ? etudiant.getSpecialite() : "");
        } else if (staff instanceof IntervenantEnseignant enseignant) {
            jsonObjectBuilder.add("type", "TEACHER");
            jsonObjectBuilder.add("schoolType", enseignant.getTypeEtablissement() != null ? enseignant.getTypeEtablissement() : "");
        } else if (staff instanceof IntervenantAutre autre) {
            jsonObjectBuilder.add("type", "OTHER");
            jsonObjectBuilder.add("activity", autre.getActivite() != null ? autre.getActivite() : "");
        } else {
            jsonObjectBuilder.add("type", "UNKNOWN");
        }

        response.getWriter().print(jsonObjectBuilder.build().toString());
    }
}
