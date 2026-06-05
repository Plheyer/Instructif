/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * "Fausse" classe de service pour simulation 
 */
public class ServiceTest {
     public List<DemandeTest> listerDemandes() {
        List<DemandeTest> result = Arrays.asList(
                new DemandeTest(15478l, LocalDateTime.parse("2026-04-20T10:30:00"), "Première Demande"),
                new DemandeTest(25552l, LocalDateTime.parse("2026-04-21T09:45:00"), "Deuxième Demande"),
                new DemandeTest(35548l, LocalDateTime.parse("2026-04-22T14:15:00"), "Troisième Demande"));
        return result;
    }
    
}