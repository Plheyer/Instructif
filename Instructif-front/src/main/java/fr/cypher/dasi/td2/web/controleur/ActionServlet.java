/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package fr.cypher.dasi.td2.web.controleur;

import com.samtheo.instructif.dao.JpaUtil;
import fr.cypher.dasi.td2.web.modele.ConsulterListeDemandesAction;
import fr.cypher.dasi.td2.web.vue.ListeDemandesSerialisation;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
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
        System.out.println(todo);
        new ConsulterListeDemandesAction().execute(request);
        
        response.setContentType("text/html;charset=UTF-8");
        new ListeDemandesSerialisation().appliquer(request, response);
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        // <editor-fold defaultstate="collapsed" desc="Compiled Code">
        /* 0: aload_0
         * 1: aload_1
         * 2: invokespecial #2                  // Method jakarta/servlet/GenericServlet.init:(Ljakarta/servlet/ServletConfig;)V
         * 5: aload_0
         * 6: aload_1
         * 7: ldc           #4                  // String jakarta.servlet.http.legacyDoHead
         * 9: invokeinterface #5,  2            // InterfaceMethod jakarta/servlet/ServletConfig.getInitParameter:(Ljava/lang/String;)Ljava/lang/String;
         * 14: invokestatic  #6                  // Method java/lang/Boolean.parseBoolean:(Ljava/lang/String;)Z
         * 17: putfield      #7                  // Field legacyHeadHandling:Z
         * 20: return
         *  */
        // </editor-fold>
        JpaUtil.creerFabriquePersistance();
    }
    
    @Override
    public void destroy() {
        JpaUtil.fermerFabriquePersistance();
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
