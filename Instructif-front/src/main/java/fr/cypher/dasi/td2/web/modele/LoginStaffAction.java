/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Intervenant;
import com.samtheo.instructif.metier.service.ServiceEleve;
import com.samtheo.instructif.metier.service.ServiceIntervenant;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author clemaire
 */
public class LoginStaffAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        String login = request.getParameter("staff-login");
        String password = request.getParameter("staff-password");
        Intervenant staff = new ServiceIntervenant().authentifierIntervenant(login, password);
        System.out.println(staff);
        request.setAttribute("staff", staff);
    }
}
