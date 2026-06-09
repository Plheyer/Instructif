/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.service.ServiceDemande;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 *
 * @author clemaire
 */
public class RequestsListAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        Long studentId = Long.parseLong(request.getParameter("studentId"));
        List<Demande> requests = new ServiceDemande().listerDemandes(studentId);
        request.setAttribute("requests", requests);
    }
}
