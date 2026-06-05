package com.samtheo.instructif.util;

import com.samtheo.instructif.metier.modele.Etablissement;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Client de l'API publique data.education.gouv.fr.
 * <ul>
 *   <li>A — adresse & géolocalisation : fr-en-adresse-et-geolocalisation-
 *       etablissements-premier-et-second-degre</li>
 *   <li>B — IPS collèges : fr-en-ips_colleges</li>
 * </ul>
 */
public final class ApiEtablissement {

    private static final String URL_GEO =
            "https://data.education.gouv.fr/api/explore/v2.1/catalog/datasets/"
            + "fr-en-adresse-et-geolocalisation-etablissements-premier-et-second-degre/records";

    private static final String URL_IPS =
            "https://data.education.gouv.fr/api/explore/v2.1/catalog/datasets/"
            + "fr-en-ips_colleges/records";

    private ApiEtablissement() {
    }

    /**
     * Construit un Etablissement à partir des deux endpoints publics.
     * Retourne null si le code UAI est introuvable ou si l'API est indispo.
     * L'entité retournée n'est PAS persistée (id == null).
     */
    public static Etablissement recupererParUAI(String codeUAI) {
        if (codeUAI == null || codeUAI.isBlank()) return null;

        JsonObject geo = appellerGeo(codeUAI);
        if (geo == null) return null;

        Etablissement e = new Etablissement();
        e.setCodeUAI(codeUAI);
        e.setAppellationOfficielle(lireString(geo, "appellation_officielle"));
        e.setSecteur(lireString(geo, "secteur_public_prive_libe"));
        e.setAdresse(lireString(geo, "adresse_uai"));
        e.setCodePostal(lireString(geo, "code_postal_uai"));
        e.setCommune(lireString(geo, "libelle_commune"));
        e.setLatitude(lireDouble(geo, "latitude"));
        e.setLongitude(lireDouble(geo, "longitude"));
        e.setLibelleDepartement(lireString(geo, "libelle_departement"));
        e.setLibelleAcademie(lireString(geo, "libelle_academie"));

        JsonObject ips = appellerIps(codeUAI);
        if (ips != null) {
            e.setIps(lireDouble(ips, "ips"));
        }
        return e;
    }

    private static JsonObject appellerGeo(String codeUAI) {
        String url = URL_GEO
                + "?refine=" + URLEncoder.encode("numero_uai:" + codeUAI, StandardCharsets.UTF_8);
        return premierRecord(url);
    }

    private static JsonObject appellerIps(String codeUAI) {
        String url = URL_IPS
                + "?refine=" + URLEncoder.encode("uai:" + codeUAI, StandardCharsets.UTF_8)
                + "&order_by=" + URLEncoder.encode("rentree_scolaire desc", StandardCharsets.UTF_8)
                + "&limit=1";
        return premierRecord(url);
    }

    private static JsonObject premierRecord(String url) {
        try {
            URI requestUri = URI.create(url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(requestUri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP " + response.statusCode() + " sur " + url);
            }
            try (JsonReader reader = Json.createReader(new StringReader(response.body()))) {
                JsonObject root = reader.readObject();
                JsonArray results = root.getJsonArray("results");
                if (results == null || results.isEmpty()) {
                    return null;
                }
                return results.getJsonObject(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    private static String lireString(JsonObject obj, String cle) {
        if (obj == null || !obj.containsKey(cle)) return null;
        JsonValue v = obj.get(cle);
        if (v == null || v.getValueType() == JsonValue.ValueType.NULL) return null;
        if (v.getValueType() == JsonValue.ValueType.STRING) return obj.getString(cle);
        return v.toString();
    }

    private static double lireDouble(JsonObject obj, String cle) {
        if (obj == null || !obj.containsKey(cle)) return 0.0;
        JsonValue v = obj.get(cle);
        if (v == null || v.getValueType() == JsonValue.ValueType.NULL) return 0.0;
        switch (v.getValueType()) {
            case NUMBER:
                return obj.getJsonNumber(cle).doubleValue();
            case STRING:
                try {
                    return Double.parseDouble(obj.getString(cle));
                } catch (NumberFormatException ex) {
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }
}
