/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.service.ServiceEleve;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author clemaire
 */
public class LoginStudentAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        String email = request.getParameter("student-email");
        String password = request.getParameter("student-password");
        Eleve eleve = new ServiceEleve().authentifierEleve(email, password);
        System.out.println(eleve);
        HttpSession session = request.getSession();
        if (session != null) session.setAttribute("eleve", eleve);
        request.setAttribute("eleve", eleve);
    }
}
