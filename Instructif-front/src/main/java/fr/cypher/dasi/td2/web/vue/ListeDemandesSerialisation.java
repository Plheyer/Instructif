/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.vue;

import fr.cypher.dasi.td2.web.test.DemandeTest;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author clemaire
 */
public class ListeDemandesSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        List<DemandeTest> listeDemandes = (List<DemandeTest>)request.getAttribute("demandes");
        
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (DemandeTest dt : listeDemandes) {
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            jsonObjectBuilder.add("id", dt.getId());
            jsonObjectBuilder.add("description", dt.getDescription());
            jsonObjectBuilder.add("dateCreation", dt.getDateCreation().toString());
            jsonArrayBuilder.add(jsonObjectBuilder);
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        response.getWriter().print(jsonArray.toString());
    }
    
}
