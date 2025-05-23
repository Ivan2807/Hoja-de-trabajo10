import java.io.*;
import java.util.List;
import java.util.Scanner;

 class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grafo grafo = new Grafo();

        try {
            grafo.leerArchivo("logistica.txt");
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Ruta más corta entre dos ciudades");
            System.out.println("2. Centro del grafo");
            System.out.println("3. Cambiar clima actual");
            System.out.println("4. Mostrar matriz de distancias");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ciudad origen: ");
                    String origen = sc.nextLine();
                    System.out.print("Ciudad destino: ");
                    String destino = sc.nextLine();
                    List<String> ruta = grafo.obtenerRuta(origen, destino);
                    double distancia = grafo.distanciaEntre(origen, destino);
                    if (ruta.isEmpty()) {
                        System.out.println("No hay ruta disponible.");
                    } else {
                        System.out.println("Ruta más corta: " + ruta);
                        System.out.println("Distancia: " + distancia);
                    }
                    break;
                case 2:
                    System.out.println("Centro del grafo: " + grafo.obtenerCentro());
                    break;
                case 3:
                    System.out.println("Climas disponibles: NORMAL, LLUVIA, NIEVE, TORMENTA");
                    System.out.print("Ingrese nuevo clima: ");
                    String nuevo = sc.nextLine().toUpperCase();
                    try {
                        grafo.cambiarClima(Clima.valueOf(nuevo));
                        System.out.println("Clima cambiado exitosamente.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Clima no válido.");
                    }
                    break;
                case 4:
                    grafo.mostrarMatriz();
                    break;
                case 5:
                    System.out.println("Programa finalizado.");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}
