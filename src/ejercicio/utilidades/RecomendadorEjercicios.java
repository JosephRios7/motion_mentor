package ejercicio.utilidades;

import ejercicio.models.Ejercicio;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class RecomendadorEjercicios {

    private List<Ejercicio> listaEjercicios;

    // Constructor que inicializa la lista de ejercicios
    public RecomendadorEjercicios(List<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    public RecomendadorEjercicios() {
    }

    // Método para recomendar ejercicios utilizando KNN
    public List<Ejercicio> recomendarEjercicios(String tipoDeseado, String dificultadDeseada) {
        int k = 5;
        System.out.println("KNN: " + k);
        List<Ejercicio> recomendaciones = new ArrayList<>();

        // Calcula la distancia entre el ejercicio deseado y todos los demás ejercicios
        Map<Ejercicio, Double> distanciaMap = new HashMap<>();
        for (Ejercicio ejercicio : listaEjercicios) {
            // Filtra ejercicios por tipo y dificultad
            if (ejercicio.getTipo().equalsIgnoreCase(tipoDeseado) && ejercicio.getDificultad().equalsIgnoreCase(dificultadDeseada)) {
                double distancia = calcularDistancia(ejercicio, tipoDeseado, dificultadDeseada);
                distanciaMap.put(ejercicio, distancia);
            }
        }

        // Ordena los ejercicios por distancia ascendente
        List<Map.Entry<Ejercicio, Double>> sortedDistances = new ArrayList<>(distanciaMap.entrySet());
        sortedDistances.sort(Comparator.comparing(Map.Entry::getValue));

        // Obtiene los k ejercicios más cercanos
        for (int i = 0; i < Math.min(k, sortedDistances.size()); i++) {
            recomendaciones.add(sortedDistances.get(i).getKey());
        }

        return recomendaciones;
    }

    private double calcularDistancia(Ejercicio ejercicio, String tipoDeseado, String dificultadDeseada) {
        double distanciaTipo = 0.0;
        double distanciaDificultad = 0.0;

        // Calcular la distancia en la dimensión del tipo de ejercicio
        if (!ejercicio.getTipo().equalsIgnoreCase(tipoDeseado)) {
            distanciaTipo = 1.0;
        }

        // Calcular la distancia en la dimensión de la dificultad
        if (!ejercicio.getDificultad().equalsIgnoreCase(dificultadDeseada)) {
            distanciaDificultad = 1.0;
        }

        // Calcular la distancia euclidiana
        double distancia = Math.sqrt(Math.pow(distanciaTipo, 2) + Math.pow(distanciaDificultad, 2));

        return distancia;
    }

public int encontrarMejorK() {
    int mejorK = 1;
    double mejorPrecision = 0.0;

    // Divide los datos en conjunto de entrenamiento y prueba
    List<Ejercicio> conjuntoEntrenamiento = new ArrayList<>(listaEjercicios);
    List<Ejercicio> conjuntoPrueba = new ArrayList<>(listaEjercicios);

    // Tamaño del conjunto de prueba (20% de los datos)
    int tamanoPrueba = (int) (listaEjercicios.size() * 0.2);

    // Iterar sobre diferentes valores de k
    for (int k = 1; k <= 10; k++) {
        double precisionPromedio = 0.0;

        // Realizar varias rondas de validación cruzada
        for (int ronda = 0; ronda < 10; ronda++) {
            // Mezclar y dividir aleatoriamente los datos en conjunto de entrenamiento y prueba
            Collections.shuffle(conjuntoPrueba);
            List<Ejercicio> conjuntoPruebaActual = conjuntoPrueba.subList(0, tamanoPrueba);
            List<Ejercicio> conjuntoEntrenamientoActual = new ArrayList<>(conjuntoEntrenamiento);
            conjuntoEntrenamientoActual.removeAll(conjuntoPruebaActual);

            // Entrenar el modelo KNN con el conjunto de entrenamiento actual
            RecomendadorEjercicios recomendador = new RecomendadorEjercicios(conjuntoEntrenamientoActual);

            // Evaluar el modelo en el conjunto de prueba actual
            int correctos = 0;
            for (Ejercicio ejercicio : conjuntoPruebaActual) {
                List<Ejercicio> recomendaciones = recomendador.recomendarEjercicios(ejercicio.getTipo(), ejercicio.getDificultad());
                if (recomendaciones.contains(ejercicio)) {
                    correctos++;
                }
            }
            // Calcular la precisión promedio para esta ronda de validación cruzada
            double precisionRonda = (double) correctos / tamanoPrueba;
            precisionPromedio += precisionRonda;
        }

        // Calcular la precisión promedio para todos los pliegues de validación cruzada
        double precisionMedia = precisionPromedio / 10;

        // Actualizar el mejor valor de k si la precisión promedio es mejor
        if (precisionMedia > mejorPrecision) {
            mejorPrecision = precisionMedia;
            mejorK = k;
        }
    }

    return mejorK;
}


}
