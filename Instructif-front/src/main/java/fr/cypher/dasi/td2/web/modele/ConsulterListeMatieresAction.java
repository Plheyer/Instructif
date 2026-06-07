/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Matiere;
import com.samtheo.instructif.metier.service.ServiceCatalogue;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 *
 * @author clemaire
 */
public class ConsulterListeMatieresAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        ServiceCatalogue service = new ServiceCatalogue();
        List<Matiere> matieres = service.listerMatieres();
        matieres.forEach(m -> m.setThemes(service.listerThemes(m.getId())));
        request.setAttribute("matieres", matieres);
    }
}
