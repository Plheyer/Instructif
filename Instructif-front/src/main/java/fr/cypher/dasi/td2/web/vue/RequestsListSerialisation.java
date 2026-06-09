package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Demande;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RequestsListSerialisation extends Serialisation {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        List<Demande> requests = (List<Demande>) request.getAttribute("requests");

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        if (requests == null) requests = List.of();
        for (Demande d : requests) {
            JsonObjectBuilder obj = Json.createObjectBuilder();
            obj.add("id", d.getId());
            obj.add("startDate", d.getDateHeureDebut() != null ? DATE_FORMAT.format(d.getDateHeureDebut()) : "");
            obj.add("endDate", d.getDateHeureFin() != null ? DATE_FORMAT.format(d.getDateHeureFin()) : "");
            obj.add("subject", d.getTheme().getMatiere().getNom());
            obj.add("topic", d.getTheme().getIntitule());
            obj.add("status", d.getStatut().name());
            arrayBuilder.add(obj);
        }
        JsonArray jsonArray = arrayBuilder.build();
        response.getWriter().print(jsonArray.toString());
    }
}
