package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.service.ServiceDemande;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class DetailDemandeAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
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

        request.setAttribute("demande", demande);
    }
}
