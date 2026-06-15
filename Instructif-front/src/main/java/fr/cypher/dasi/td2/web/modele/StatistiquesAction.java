package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.dto.StatistiquesReseau;
import com.samtheo.instructif.metier.modele.Etablissement;
import com.samtheo.instructif.metier.service.ServiceStatistique;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

public class StatistiquesAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        StatistiquesReseau stats = new ServiceStatistique().calculerStatistiques();
        Set<Etablissement> etab = new ServiceStatistique().getEtablissements();
        request.setAttribute("stats", stats);
        request.setAttribute("etab", etab);
    }
}
