package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Bilan;
import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.service.ServiceBilan;
import com.samtheo.instructif.metier.service.ServiceDemande;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class EnvoyerBilanAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        Intervenant staff = (Intervenant) session.getAttribute("staff");
        if (staff == null) return;

        String idStr  = request.getParameter("id");
        String texte  = request.getParameter("texte");
        String conseils = request.getParameter("conseils");

        if (idStr == null || texte == null) return;
        long idDemande;
        try {
            idDemande = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return;
        }

        // Same ownership guard as DetailDemandeAction.
        // ServiceBilan.envoyerBilan checks intervenant != null but not identity,
        // so the layer above must enforce it.
        ServiceDemande serviceDemande = new ServiceDemande();
        Demande demande = serviceDemande.trouverDemandeParId(idDemande);
        if (demande == null || demande.getIntervenant() == null
                || !demande.getIntervenant().getId().equals(staff.getId())) {
            return;
        }

        ServiceBilan serviceBilan = new ServiceBilan();
        Bilan bilan = serviceBilan.envoyerBilan(idDemande, texte, conseils);
        request.setAttribute("success", bilan != null);
    }
}
