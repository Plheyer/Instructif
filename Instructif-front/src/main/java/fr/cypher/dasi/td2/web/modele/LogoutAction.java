/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Intervenant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author clemaire
 */
public class LogoutAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) return;
        try {
            String key = request.getParameter("key");
            System.out.println(key);
            session.removeAttribute(key);
            System.out.println(session.getAttribute(key));
        } catch (Exception ignored) {}
    }
}
