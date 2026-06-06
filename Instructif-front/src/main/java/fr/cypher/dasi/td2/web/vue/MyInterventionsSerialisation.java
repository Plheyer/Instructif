package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Demande;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyInterventionsSerialisation extends Serialisation {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        List<Demande> interventions = (List<Demande>) request.getAttribute("interventions");
        Demande currentAssignment = (Demande) request.getAttribute("currentAssignment");

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        if (interventions != null) {
            for (Demande d : interventions) {
                JsonObjectBuilder obj = Json.createObjectBuilder();
                obj.add("id", d.getId());
                obj.add("startDate", d.getDateHeureDebut() != null ? DATE_FORMAT.format(d.getDateHeureDebut()) : "");
                obj.add("endDate", d.getDateHeureFin() != null ? DATE_FORMAT.format(d.getDateHeureFin()) : "");
                obj.add("studentLastName", d.getEleve().getNom());
                obj.add("studentFirstName", d.getEleve().getPrenom());
                obj.add("studentGrade", d.getEleve().getNiveau());
                obj.add("subject", d.getTheme().getMatiere().getNom());
                obj.add("topic", d.getTheme().getIntitule());
                obj.add("status", d.getStatut().name());
                arrayBuilder.add(obj);
            }
        }

        JsonObjectBuilder root = Json.createObjectBuilder();
        root.add("interventions", arrayBuilder);

        if (currentAssignment != null) {
            JsonObjectBuilder assignment = Json.createObjectBuilder();
            assignment.add("id", currentAssignment.getId());
            assignment.add("studentLastName", currentAssignment.getEleve().getNom());
            assignment.add("studentFirstName", currentAssignment.getEleve().getPrenom());
            assignment.add("studentGrade", currentAssignment.getEleve().getNiveau());
            assignment.add("subject", currentAssignment.getTheme().getMatiere().getNom());
            assignment.add("topic", currentAssignment.getTheme().getIntitule());
            assignment.add("meetingLink", currentAssignment.getLienVisio() != null ? currentAssignment.getLienVisio() : "");
            root.add("currentAssignment", assignment);
        } else {
            root.addNull("currentAssignment");
        }

        response.getWriter().print(root.build().toString());
    }
}
