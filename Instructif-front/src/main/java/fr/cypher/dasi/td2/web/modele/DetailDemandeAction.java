package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.service.ServiceDemande;
import com.samtheo.instructif.metier.service.ServiceIntervenant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public class DetailDemandeAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        Intervenant staff = (Intervenant) session.getAttribute("staff");
        if (staff == null) return;

        String idStr = request.getParameter("id");
        if (idStr == null) return;
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return;
        }

        ServiceDemande serviceDemande = new ServiceDemande();
        Demande demande = serviceDemande.trouverDemandeParId(id);

        if (demande == null || demande.getIntervenant() == null
                || !demande.getIntervenant().getId().equals(staff.getId())) {
            return;
        }

        ServiceIntervenant serviceIntervenant = new ServiceIntervenant();
        List<Demande> recentes = serviceIntervenant.listerInterventions(staff.getId());

        request.setAttribute("demande", demande);
        request.setAttribute("recentes", recentes);
    }
}
