/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Theme;
import com.samtheo.instructif.metier.service.ServiceCatalogue;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 *
 * @author clemaire
 */
public class GetThemesAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        Long subjectId = Long.parseLong(request.getParameter("subjectId"));
        List<Theme> themes = new ServiceCatalogue().listerThemes(subjectId);
        request.setAttribute("themes", themes);
    }
}
