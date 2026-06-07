/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Matiere;
import com.samtheo.instructif.metier.modele.Theme;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author clemaire
 */
public class ListeMatieresSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            List<Matiere> listeMatieres = (List<Matiere>) request.getAttribute("matieres");

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Matiere matiere : listeMatieres) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("id", matiere.getId());
                jsonObjectBuilder.add("name", matiere.getNom());

                JsonArrayBuilder jsonThemesArrayBuilder = Json.createArrayBuilder();
                for (Theme theme : matiere.getThemes()) {
                    JsonObjectBuilder jsonThemeObjectBuilder = Json.createObjectBuilder();
                    jsonThemeObjectBuilder.add("id", theme.getId());
                    jsonThemeObjectBuilder.add("intitule", theme.getIntitule());
                    jsonThemesArrayBuilder.add(jsonThemeObjectBuilder);
                }
                jsonObjectBuilder.add("themes", jsonThemesArrayBuilder);
                jsonArrayBuilder.add(jsonObjectBuilder);
            }
            JsonArray jsonArray = jsonArrayBuilder.build();
            response.getWriter().print(jsonArray.toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.getWriter().print("{[]}");
        }
    }
    
}
