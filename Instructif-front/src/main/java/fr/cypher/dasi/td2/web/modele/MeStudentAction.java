/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.service.ServiceEleve;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author clemaire
 */
public class MeStudentAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            request.setAttribute("eleve", null);
            return;
        }
        try {
            Eleve eleve = (Eleve) request.getAttribute("eleve");
            request.setAttribute("eleve", eleve);
        } catch (Exception e) {
            request.setAttribute("eleve", null);
        }
    }

    private static Date parseDate(String iso) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(iso);
        } catch (Exception ex) {
            Calendar c = Calendar.getInstance();
            c.set(2010, 0, 1);
            return c.getTime();
        }
    }
}
