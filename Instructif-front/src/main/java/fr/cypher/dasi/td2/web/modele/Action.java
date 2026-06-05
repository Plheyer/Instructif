/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.cypher.dasi.td2.web.modele;

import jakarta.servlet.http.HttpServletRequest;

public abstract class Action {

    public Action() {
    }

    public abstract void execute(HttpServletRequest request);
}