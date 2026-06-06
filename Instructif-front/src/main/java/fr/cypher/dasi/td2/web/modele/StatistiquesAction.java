package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.dto.StatistiquesReseau;
import com.samtheo.instructif.metier.service.ServiceStatistique;
import jakarta.servlet.http.HttpServletRequest;

public class StatistiquesAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        StatistiquesReseau stats = new ServiceStatistique().calculerStatistiques();
        System.out.println("[Statistiques] nombreSoutiens=" + stats.nombreSoutiens());
        System.out.println("[Statistiques] dureeMoyenneMinutes=" + stats.dureeMoyenneMinutes());
        System.out.println("[Statistiques] parAcademie=" + stats.etablissementsParAcademie());
        System.out.println("[Statistiques] parTrancheIps=" + stats.etablissementsParTrancheIps());
        request.setAttribute("stats", stats);
    }
}
