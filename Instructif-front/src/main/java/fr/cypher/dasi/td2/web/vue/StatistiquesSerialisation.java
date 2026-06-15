package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.dto.StatistiquesReseau;
import com.samtheo.instructif.metier.modele.Etablissement;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatistiquesSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        StatistiquesReseau stats = (StatistiquesReseau) request.getAttribute("stats");
        Set<Etablissement> etab = (Set<Etablissement>) request.getAttribute("etab");
        if (stats == null || etab == null) {
            response.getWriter().print("null");
            return;
        }

        JsonObjectBuilder root = Json.createObjectBuilder();
        root.add("nombreSoutiens", stats.nombreSoutiens());
        root.add("dureeMoyenneMinutes", Math.round(stats.dureeMoyenneMinutes()));

        root.add("parAcademie", toArray(stats.etablissementsParAcademie()));
        root.add("parTrancheIps", toArray(stats.etablissementsParTrancheIps()));
        
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (Etablissement e : etab) {
            arr.add(Json.createObjectBuilder().add("latitude", e.getLatitude()).add("longitude", e.getLongitude()).add("name", e.getAppellationOfficielle()));
        }
        root.add("parCarte", arr);

        response.getWriter().print(root.build().toString());
    }

    private static JsonArrayBuilder toArray(Map<String, Long> map) {
        JsonArrayBuilder arr = Json.createArrayBuilder();
        map.forEach((label, count) -> arr.add(
                Json.createObjectBuilder().add("label", label).add("count", count)));
        return arr;
    }
}
