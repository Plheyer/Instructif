package fr.cypher.dasi.td2.web.vue;

import com.samtheo.instructif.metier.modele.Theme;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ThemesSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        List<Theme> themes = (List<Theme>) request.getAttribute("themes");

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        if (themes == null) themes = List.of();
        for (Theme theme : themes) {
            JsonObjectBuilder jsonThemeObjectBuilder = Json.createObjectBuilder();
            jsonThemeObjectBuilder.add("id", theme.getId());
            jsonThemeObjectBuilder.add("intitule", theme.getIntitule());
            arrayBuilder.add(jsonThemeObjectBuilder);
        }
        JsonArray jsonArray = arrayBuilder.build();
        response.getWriter().print(jsonArray.toString());
    }
}
