package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.service.ServiceIntervenant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public class MyInterventionsAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("interventions", List.of());
            request.setAttribute("currentAssignment", null);
            return;
        }
        Intervenant staff = (Intervenant) session.getAttribute("staff");
        if (staff == null) {
            request.setAttribute("interventions", List.of());
            request.setAttribute("currentAssignment", null);
            return;
        }
        ServiceIntervenant service = new ServiceIntervenant();
        List<Demande> interventions = service.listerInterventions(staff.getId());
        Demande currentAssignment = service.trouverAffectationCourante(staff.getId());
        request.setAttribute("interventions", interventions);
        request.setAttribute("currentAssignment", currentAssignment);
    }
}
