package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.dto.StatistiquesReseau;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class StatistiquesSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        StatistiquesReseau stats = (StatistiquesReseau) request.getAttribute("stats");
        if (stats == null) {
            response.getWriter().print("null");
            return;
        }

        JsonObjectBuilder root = Json.createObjectBuilder();
        root.add("nombreSoutiens", stats.nombreSoutiens());
        root.add("dureeMoyenneMinutes", Math.round(stats.dureeMoyenneMinutes()));

        root.add("parAcademie", toArray(stats.etablissementsParAcademie()));
        root.add("parTrancheIps", toArray(stats.etablissementsParTrancheIps()));

        response.getWriter().print(root.build().toString());
    }

    private static JsonArrayBuilder toArray(Map<String, Long> map) {
        JsonArrayBuilder arr = Json.createArrayBuilder();
        map.forEach((label, count) -> arr.add(
                Json.createObjectBuilder().add("label", label).add("count", count)));
        return arr;
    }
}
