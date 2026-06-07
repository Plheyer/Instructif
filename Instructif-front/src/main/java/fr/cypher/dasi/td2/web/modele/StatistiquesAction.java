package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.dto.StatistiquesReseau;
import com.samtheo.instructif.metier.service.ServiceStatistique;
import jakarta.servlet.http.HttpServletRequest;

public class StatistiquesAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        StatistiquesReseau stats = new ServiceStatistique().calculerStatistiques();
        request.setAttribute("stats", stats);
    }
}
