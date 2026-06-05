/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import fr.cypher.dasi.td2.web.test.DemandeTest;
import fr.cypher.dasi.td2.web.test.ServiceTest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author clemaire
 */
public class ConsulterListeDemandesAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        List<DemandeTest> demandes = new ServiceTest().listerDemandes();
        demandes.forEach(System.out::println);
        request.setAttribute("demandes", demandes);
    }
    
}
