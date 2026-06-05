/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.service.ServiceEleve;
import jakarta.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author clemaire
 */
public class RegisterAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String date = request.getParameter("date");
        Date dateParsed = parseDate(date);
        int schoolGrade = Integer.parseInt(request.getParameter("schoolGrade"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String uai = request.getParameter("uai");
        Eleve eleve = new Eleve(lastName, firstName, dateParsed, email, password, schoolGrade);
        boolean result = new ServiceEleve().inscrireEleve(eleve, uai);
        request.setAttribute("result", result);
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
