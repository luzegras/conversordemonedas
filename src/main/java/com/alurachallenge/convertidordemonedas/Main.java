package com.alurachallenge.convertidordemonedas;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Main {

    //Color
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        //Titulo
        System.out.println(YELLOW + "\n[Conversor de Monedas]\n" + RESET);

        // URL de Exchangerate API / tasas de cambio
        String url = "https://v6.exchangerate-api.com/v6/5c793fb38af441f5483837c0/latest/USD";

        // cliente HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // iniciar solicitud y  respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Convertir JSON
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

            // Tasas de cambio
            JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");

            // códigos de monedas
            Map<String, String> monedascode = new HashMap<>();
            monedascode.put("ARS", "Peso argentino");
            monedascode.put("BOB", "Boliviano boliviano");
            monedascode.put("BRL", "Real brasileño");
            monedascode.put("CLP", "Peso chileno");
            monedascode.put("COP", "Peso colombiano");
            monedascode.put("PAB", "Balboa panameño");

            // Filtro de monedas
            mostrarTasasDeCambio(rates, monedascode, "ARS", "BOB", "BRL", "CLP", "COP", "PAB");

            // Entrada del usuario
            Scanner scanner = new Scanner(System.in);

            boolean continuar = true;

            while (continuar) {
                
                // Menú de selección
                System.out.println("\nElige una opción para convertir:");
                System.out.println(GREEN+"1. Convertir USD a tu moneda");
                System.out.println(BLUE+"2. Convertir de tu moneda a USD");
                System.out.println(RED+"3. Salir"+RESET);
                System.out.print("Selecciona una opción ("
                        +  GREEN + "1" + RESET + ", "
                        + BLUE + "2" + RESET + " o "
                        + RED + "3" + RESET + "): ");
                int opcion = scanner.nextInt();

                // Mostrar el menú de selección de moneda y realizar la conversión
                if (opcion == 1 || opcion == 2) {
                    // Mostrar el menú de selección de moneda
                    if(opcion==1){System.out.printf( GREEN+"\n[De USD a tu moneda]\n"+RESET);}
                    else{System.out.printf(BLUE+ "\n[De tu moneda a USD]\n"+RESET);}
                    while (true) {
                        System.out.printf( "\nElige Tu moneda" +BLUE+" (número):\n" + RESET);

                        // Mostrar las opciones de moneda
                        int i = 1;
                        for (String moneda : monedascode.keySet()) {
                            System.out.println(BLUE + i + ". " + RESET + monedascode.get(moneda) + GREEN+ " (" + moneda + ")" + RESET);
                            i++;
                        }

                        // Opciones adicionales
                        System.out.println(i + YELLOW +". Volver al menú principal"+ RESET);
                        System.out.println((i + 1) + RED +". Salir" + RESET);

                        // Leer la selección
                        System.out.print("Selecciona una Opcion  "+BLUE+"(número): " +RESET);
                        int monedaSeleccionada = scanner.nextInt();

                        // Opción 7: Volver al menú principal
                        if (monedaSeleccionada == i) {
                            break;
                        }

                        // Opción 8: Terminar el bucle
                        if (monedaSeleccionada == (i + 1)) {
                            System.out.println("Gracias por usar el Conversor de Monedas Alura Challenge, ¡Hasta la vista, BABY!");
                            continuar = false;
                            break;
                        }

                        // Validar la entrada
                        if (monedaSeleccionada >= 1 && monedaSeleccionada <= monedascode.size()) {
                            String[] monedas = monedascode.keySet().toArray(new String[0]);
                            String monedaSeleccionadaCodigo = monedas[monedaSeleccionada - 1];
                            String converterStr = "";
                            if (opcion == 1){ converterStr = "USD";}else{converterStr = monedaSeleccionadaCodigo;}
                            System.out.print("Introduce la cantidad a convertir en "+ converterStr +": ");
                            double cantidad = scanner.nextDouble();

                            // Conversión
                            if (opcion == 1) {
                                // Convertir de USD a la moneda seleccionada
                                System.out.printf(BLUE + "\nDe USD a " + monedaSeleccionadaCodigo + "\n" + RESET);
                                JsonElement tasa = rates.get(monedaSeleccionadaCodigo);
                                if (tasa != null && tasa.isJsonPrimitive()) {
                                    double resultado = cantidad * tasa.getAsDouble();
                                    System.out.printf("-----------------------------"+GREEN+"[Resultado]"+RESET+"--------------------------------------------\n");
                                    System.out.printf(RED + "%.2f USD --> %.2f %s.\n", cantidad, resultado, monedaSeleccionadaCodigo );
                                    System.out.printf(RESET+"------------------------------------------------------------------------------------\n");
                                }
                            } else {
                                // Convertir de la moneda seleccionada a USD
                                System.out.printf(GREEN + "\nDe " + monedaSeleccionadaCodigo + " a USD\n" +RESET);
                                JsonElement tasa = rates.get(monedaSeleccionadaCodigo);
                                if (tasa != null && tasa.isJsonPrimitive()) {
                                    double resultado = cantidad / tasa.getAsDouble();
                                    System.out.printf("-----------------------------"+GREEN+"[Resultado]"+RESET+"--------------------------------------------\n");
                                    System.out.printf(RED + "%.2f %s --> %.2f USD.\n", cantidad, monedaSeleccionadaCodigo, resultado);
                                    System.out.printf(RESET+"------------------------------------------------------------------------------------\n");
                                }
                            }
                            break;  // Salir del bucle para continuar el menú de conversión
                        } else {
                            System.out.println("Por favor, selecciona un número válido de la lista.");
                        }
                    }
                } else if (opcion == 3) {
                    // Salir del programa
                    System.out.println("Gracias por usar el Conversor de Monedas Alura Challenge, ¡Hasta la vista, BABY!");
                    continuar = false;
                } else {
                    System.out.println("-Opción no válida. Por favor, selecciona una opción válida.");
                }
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al obtener las tasas de cambio: " + e.getMessage());
        }
    }

    // Metodo
    public static void mostrarTasasDeCambio(JsonObject rates, Map<String, String> monedascode, String... monedas) {
        System.out.printf(GREEN + "[Tasa de cambio]\n");
        for (String moneda : monedas) {
            JsonElement tasa = rates.get(moneda);
            if (tasa != null && tasa.isJsonPrimitive()) {

                // Tasa de cambio por moneda
                String nombreMoneda = monedascode.getOrDefault(moneda, "Desconocido");

                System.out.printf(BLUE + "%s (%s): " + RESET + GREEN + "%.2f\n" + RESET, nombreMoneda, moneda, tasa.getAsDouble());
            } else {
                System.out.printf("La moneda %s no está disponible.\n", moneda);
            }
        }
    }
}
