/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package fr.cypher.dasi.td2.web.controleur;

import com.samtheo.instructif.dao.JpaUtil;
import fr.cypher.dasi.td2.web.modele.*;
import fr.cypher.dasi.td2.web.vue.RegisterSerialisation;
import fr.cypher.dasi.td2.web.vue.ListeDemandesSerialisation;

import java.io.IOException;

import fr.cypher.dasi.td2.web.vue.StaffSerialisation;
import fr.cypher.dasi.td2.web.vue.StudentSerialisation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author clemaire
 */
@WebServlet(name = "ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String todo = request.getParameter("todo");
        response.setContentType("text/html;charset=UTF-8");
        System.out.println(todo);
        switch (todo) {
            case "register":
                new RegisterAction().execute(request);
                new RegisterSerialisation().appliquer(request, response);
                break;
            case "login-student":
                new LoginStudentAction().execute(request);
                new StudentSerialisation().appliquer(request, response);
                break;
            case "login-staff":
                new LoginStaffAction().execute(request);
                new StaffSerialisation().appliquer(request, response);
                break;
            case "me-student":
                new MeStudentAction().execute(request);
                new StudentSerialisation().appliquer(request, response);
                break;
            case "me-staff":
                new MeStaffAction().execute(request);
                new StaffSerialisation().appliquer(request, response);
                break;
            case "logout":
                new LogoutAction().execute(request);
                // Doesn't return anything
                break;
            default:
                new ConsulterListeDemandesAction().execute(request);
                new ListeDemandesSerialisation().appliquer(request, response);
        }
    }

    @Override
    public void destroy() {
        JpaUtil.fermerFabriquePersistance();
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        JpaUtil.creerFabriquePersistance();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
