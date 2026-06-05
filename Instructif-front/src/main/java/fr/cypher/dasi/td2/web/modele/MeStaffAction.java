/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import com.samtheo.instructif.metier.modele.Eleve;
import com.samtheo.instructif.metier.modele.Intervenant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author clemaire
 */
public class MeStaffAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            request.setAttribute("staff", null);
            return;
        }
        try {
            Intervenant staff = (Intervenant) request.getAttribute("staff");
            request.setAttribute("staff", staff);
        } catch (Exception e) {
            request.setAttribute("staff", null);
        }
    }
}
