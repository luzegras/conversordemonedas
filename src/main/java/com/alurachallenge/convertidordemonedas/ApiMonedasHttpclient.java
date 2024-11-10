package com.alurachallenge.convertidordemonedas;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiMonedasHttpclient {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/5c793fb38af441f5483837c0/latest/USD";
    private HttpClient client = HttpClient.newHttpClient();

    public JsonObject obtenerTasasDeCambio() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        return gson.fromJson(response.body(), JsonObject.class).getAsJsonObject("conversion_rates");
    }
}
