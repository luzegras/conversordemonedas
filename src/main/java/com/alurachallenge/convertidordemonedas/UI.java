package com.alurachallenge.convertidordemonedas;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private final ApiMonedasHttpclient apiService = new ApiMonedasHttpclient();
    private final ConversorDeMonedas conversor = new ConversorDeMonedas();
    private final Map<String, String> monedascode = new HashMap<>();
    private boolean continuar = true;

    public UI() {
        // Inicializa las monedas disponibles
        monedascode.put("ARS", "Peso argentino");
        monedascode.put("BOB", "Boliviano boliviano");
        monedascode.put("BRL", "Real brasileño");
        monedascode.put("CLP", "Peso chileno");
        monedascode.put("COP", "Peso colombiano");
        monedascode.put("PAB", "Balboa panameño");
    }

    public void iniciar() {

        //Mostrar Menu
        System.out.println(Colores.YELLOW + "\n[Conversor de Monedas]\n" + Colores.RESET);

        try {
            JsonObject rates = apiService.obtenerTasasDeCambio();
            mostrarTasasDeCambio(rates);

            Scanner scanner = new Scanner(System.in);

            while (continuar) {  // Menu Principal
                System.out.println("\nElige una opción para convertir:");
                System.out.println(Colores.GREEN + "1. Convertir USD a tu moneda");
                System.out.println(Colores.BLUE + "2. Convertir de tu moneda a USD");
                System.out.println(Colores.RED + "3. Salir" + Colores.RESET);
                System.out.print("Selecciona una opción (" +  Colores.GREEN + "1" + Colores.RESET + ", "
                        + Colores.BLUE + "2" + Colores.RESET + " o "
                        + Colores.RED + "3" + Colores.RESET + "): ");
                int opcion = scanner.nextInt();

                if (opcion == 1 || opcion == 2) {
                    if(opcion == 1) {
                        System.out.printf(Colores.GREEN + "\n[De USD a tu moneda]\n" + Colores.RESET);
                    } else {
                        System.out.printf(Colores.BLUE + "\n[De tu moneda a USD]\n" + Colores.RESET);
                    }

                    convertirMoneda(scanner, rates, opcion);
                } else if (opcion == 3) {
                    System.out.println("Gracias por usar el Conversor de Monedas Alura Challenge. ¡Hasta la vista, BABY!");
                    continuar = false;
                } else {
                    System.out.println("- Opción no válida.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener las tasas de cambio: " + e.getMessage());
        }
    }

    //Mostrar Tasas
    private void mostrarTasasDeCambio(JsonObject rates) {
        System.out.println(Colores.GREEN + "[Tasa de cambio]");
        for (Map.Entry<String, String> entry : monedascode.entrySet()) {
            String moneda = entry.getKey();
            JsonElement tasa = rates.get(moneda);
            if (tasa != null && tasa.isJsonPrimitive()) {
                System.out.printf(Colores.BLUE + "%s (%s): " + Colores.GREEN + "%.2f\n" + Colores.RESET, entry.getValue(), moneda, tasa.getAsDouble());
            }
        }
    }

    //Convertidor
    private void convertirMoneda(Scanner scanner, JsonObject rates, int opcion) {

        System.out.printf( "\nElige Tu moneda" +Colores.BLUE+" (número):\n" + Colores.RESET);

        int i = 1;
        for (String codigo : monedascode.keySet()) {
            System.out.println(Colores.BLUE + i + ". " + Colores.RESET + monedascode.get(codigo) + Colores.GREEN+ " (" + codigo + ")" + Colores.RESET);
            i++;
        }

        // Opciones adicionales
        System.out.println(i + Colores.YELLOW +". Volver al menú principal" + Colores.RESET);
        System.out.println((i + 1) + Colores.RED +". Salir" + Colores.RESET);

        // Leer la selección
        System.out.print("Selecciona una Opción " + Colores.BLUE + "(número): " + Colores.RESET);
        int monedaSeleccionada = scanner.nextInt();

        // Opción para volver al menú principal
        if (monedaSeleccionada == i) {
            System.out.println("Volviendo al menú principal...");
            return;
        }

        // Opción para salir
        if (monedaSeleccionada == (i + 1)) {
            System.out.println("Gracias por usar el Conversor de Monedas Alura Challenge, ¡Hasta la vista, BABY!");
            continuar = false;  // Detiene el ciclo principal
            return;
        }

        // Continuar con la selección normal de monedas
        String[] monedas = monedascode.keySet().toArray(new String[0]);
        String codigoMoneda = monedas[monedaSeleccionada - 1];

        String converterStr;
        if (opcion == 1){ converterStr = "USD";}else{converterStr = codigoMoneda;}
        System.out.print("Introduce la cantidad a convertir en "+ converterStr +": ");
        double cantidad = scanner.nextDouble();

        if (opcion == 1) {
            System.out.printf(Colores.BLUE + "\nDe USD a " + codigoMoneda + "\n" + Colores.RESET);
            double resultado = conversor.convertirDeUsd(rates, codigoMoneda, cantidad);

            System.out.printf("-----------------------------" + Colores.GREEN + "[Resultado]" + Colores.RESET + "--------------------------------------------\n");
            System.out.printf(Colores.RED + "%.2f USD -> %.2f %s\n", cantidad, resultado, codigoMoneda + Colores.RESET);
            System.out.printf(Colores.RESET + "------------------------------------------------------------------------------------\n");

        } else {
            System.out.printf(Colores.GREEN + "\nDe " + codigoMoneda + " a USD\n" + Colores.RESET);
            double resultado = conversor.convertirAUsd(rates, codigoMoneda, cantidad);

            System.out.printf("-----------------------------" + Colores.GREEN + "[Resultado]" + Colores.RESET + "--------------------------------------------\n");
            System.out.printf(Colores.RED + "%.2f %s -> %.2f USD\n", cantidad, codigoMoneda, resultado);
            System.out.printf(Colores.RESET + Colores.RESET+"------------------------------------------------------------------------------------\n");
        }
    }
}
