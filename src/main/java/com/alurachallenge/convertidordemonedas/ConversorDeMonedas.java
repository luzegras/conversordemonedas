package com.alurachallenge.convertidordemonedas;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConversorDeMonedas {

    public double convertirDeUsd(JsonObject rates, String moneda, double cantidad) {
        JsonElement tasa = rates.get(moneda);
        return tasa != null && tasa.isJsonPrimitive() ? cantidad * tasa.getAsDouble() : 0;
    }

    public double convertirAUsd(JsonObject rates, String moneda, double cantidad) {
        JsonElement tasa = rates.get(moneda);
        return tasa != null && tasa.isJsonPrimitive() ? cantidad / tasa.getAsDouble() : 0;
    }
}
