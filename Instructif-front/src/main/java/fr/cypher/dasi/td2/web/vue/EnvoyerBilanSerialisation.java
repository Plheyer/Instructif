package fr.cypher.dasi.td2.web.vue;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EnvoyerBilanSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        Boolean success = (Boolean) request.getAttribute("success");
        if (Boolean.TRUE.equals(success)) {
            response.getWriter().print("{\"success\":true}");
        } else {
            response.getWriter().print("{\"success\":false}");
        }
    }
}
