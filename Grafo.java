import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grafo {
    private List<String> ciudades = new ArrayList<>();
    private Map<String, Integer> indiceCiudad = new HashMap<>();
    private double[][][] matrizPesos; // [clima][i][j]
    private double[][] distancias;
    private int[][] predecesores;
    private Clima climaActual = Clima.NORMAL;

    public void leerArchivo(String archivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        List<String[]> conexiones = new ArrayList<>();

        while ((linea = br.readLine()) != null) {
            String[] partes = linea.trim().split("\\s+");
            if (partes.length != 6) continue;
            if (!indiceCiudad.containsKey(partes[0])) agregarCiudad(partes[0]);
            if (!indiceCiudad.containsKey(partes[1])) agregarCiudad(partes[1]);
            conexiones.add(partes);
        }
        br.close();

        int n = ciudades.size();
        matrizPesos = new double[4][n][n];
        for (double[][] matriz : matrizPesos)
            for (double[] fila : matriz) Arrays.fill(fila, Double.POSITIVE_INFINITY);

        for (String[] partes : conexiones) {
            int i = indiceCiudad.get(partes[0]);
            int j = indiceCiudad.get(partes[1]);
            matrizPesos[0][i][j] = Double.parseDouble(partes[2]);
            matrizPesos[1][i][j] = Double.parseDouble(partes[3]);
            matrizPesos[2][i][j] = Double.parseDouble(partes[4]);
            matrizPesos[3][i][j] = Double.parseDouble(partes[5]);
        }
        aplicarFloyd();
    }

    public void agregarCiudad(String nombre) {
        indiceCiudad.put(nombre, ciudades.size());
        ciudades.add(nombre);
    }

    public void cambiarClima(Clima nuevoClima) {
        this.climaActual = nuevoClima;
        aplicarFloyd();
    }

    public void aplicarFloyd() {
        int n = ciudades.size();
        distancias = new double[n][n];
        predecesores = new int[n][n];

        double[][] pesos = matrizPesos[climaActual.ordinal()];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = pesos[i][j];
                predecesores[i][j] = (i == j || pesos[i][j] == Double.POSITIVE_INFINITY) ? -1 : i;
            }
        }

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        predecesores[i][j] = predecesores[k][j];
                    }
    }

    public List<String> obtenerRuta(String origen, String destino) {
        int i = indiceCiudad.get(origen);
        int j = indiceCiudad.get(destino);
        if (distancias[i][j] == Double.POSITIVE_INFINITY) return Collections.emptyList();
        LinkedList<String> ruta = new LinkedList<>();
        reconstruirRuta(i, j, ruta);
        return ruta;
    }

    private void reconstruirRuta(int i, int j, List<String> ruta) {
        if (i == j) {
            ruta.add(ciudades.get(i));
        } else if (predecesores[i][j] == -1) {
            ruta.clear();
        } else {
            reconstruirRuta(i, predecesores[i][j], ruta);
            ruta.add(ciudades.get(j));
        }
    }

    public String obtenerCentro() {
        int n = ciudades.size();
        double minEcc = Double.POSITIVE_INFINITY;
        int centro = -1;
        for (int i = 0; i < n; i++) {
            double ecc = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && distancias[i][j] < Double.POSITIVE_INFINITY)
                    ecc = Math.max(ecc, distancias[i][j]);
            }
            if (ecc < minEcc) {
                minEcc = ecc;
                centro = i;
            }
        }
        return centro != -1 ? ciudades.get(centro) : null;
    }

    public double distanciaEntre(String origen, String destino) {
        return distancias[indiceCiudad.get(origen)][indiceCiudad.get(destino)];
    }

    public void mostrarMatriz() {
        System.out.println("Matriz de distancias (" + climaActual + "):");
        for (int i = 0; i < ciudades.size(); i++) {
            for (int j = 0; j < ciudades.size(); j++) {
                System.out.printf("%10.1f ", distancias[i][j]);
            }
            System.out.println();
        }
    }

    public void agregarConexion(String ciudad1, String ciudad2, double pesoNormal, double pesoLluvia, double pesoNieve, double pesoTormenta) {
        if (!indiceCiudad.containsKey(ciudad1)) agregarCiudad(ciudad1);
        if (!indiceCiudad.containsKey(ciudad2)) agregarCiudad(ciudad2);

        int i = indiceCiudad.get(ciudad1);
        int j = indiceCiudad.get(ciudad2);

        matrizPesos[0][i][j] = pesoNormal;
        matrizPesos[1][i][j] = pesoLluvia;
        matrizPesos[2][i][j] = pesoNieve;
        matrizPesos[3][i][j] = pesoTormenta;
        
        aplicarFloyd();
    }
}

enum Clima {
    NORMAL, LLUVIA, NIEVE, TORMENTA
}
