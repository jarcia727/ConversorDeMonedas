import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class Cliente {

    static class ApiResponse {
        String result;

        @SerializedName("conversion_rates")
        Map<String, Double> conversionRates;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            exibirMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            if (opcion >= 1 && opcion <= 6) {
                System.out.print("Ingrese el valor que desea convertir: ");
                double monto = scanner.nextDouble();

                // Llamar API
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://v6.exchangerate-api.com/v6/57cd7bab28a238850e1b3736/latest/USD"))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                Gson gson = new Gson();
                ApiResponse data = gson.fromJson(response.body(), ApiResponse.class);

                if (!"success".equalsIgnoreCase(data.result)) {
                    System.out.println("Error al obtener datos de la API.");
                    continue;
                }

                String origen = "", destino = "";
                switch (opcion) {
                    case 1: origen = "USD"; destino = "ARS"; break;
                    case 2: origen = "ARS"; destino = "USD"; break;
                    case 3: origen = "USD"; destino = "BRL"; break;
                    case 4: origen = "BRL"; destino = "USD"; break;
                    case 5: origen = "USD"; destino = "COP"; break;
                    case 6: origen = "COP"; destino = "USD"; break;
                }

                double tasaOrigen = origen.equals("USD") ? 1.0 : data.conversionRates.get(origen);
                double tasaDestino = data.conversionRates.get(destino);
                double resultado = monto / tasaOrigen * tasaDestino;

                System.out.printf("\n%.2f %s equivale a %.2f %s\n\n", monto, origen, resultado, destino);

            } else if (opcion == 7) {
                System.out.println("Gracias por usar el conversor de moneda.");
            } else {
                System.out.println("Opción inválida. Intente nuevamente.\n");
            }

        } while (opcion != 7);

        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("""
                ************************************************
                Sea bienvenido/a al Conversor de Moneda =]
                1) Dólar =>> Peso argentino
                2) Peso argentino =>> Dólar
                3) Dólar =>> Real brasileño
                4) Real brasileño =>> Dólar
                5) Dólar =>> Peso colombiano
                6) Peso colombiano =>> Dólar
                7) Salir
                Elija una opción válida:
                ************************************************
                """);
    }
}

