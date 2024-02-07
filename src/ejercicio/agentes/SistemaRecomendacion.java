package ejercicio.agentes;

import java.util.*;
import java.io.IOException;
import java.util.Collections;

public class SistemaRecomendacion {
    // Función para hablar el texto proporcionado
    private static void speakText(String textToRead) {
        try {
            // Especifica la ruta completa al ejecutable espeak
            String espeakPath = "C:\\Program Files (x86)\\eSpeak\\command_line\\espeak.exe";   
            // Reemplaza con la ubicación real de espeak.exe en tu sistema

            // Comando para ejecutar espeak desde la línea de comandos con voz en español
            String command = espeakPath + " -v es \"" + textToRead + "\"";

            // Ejecutar el comando
            Process process = Runtime.getRuntime().exec(command);

            // Esperar a que el proceso termine
            process.waitFor();

            // Imprimir el resultado del proceso (opcional)
            int exitCode = process.exitValue();
            System.out.println("El proceso de texto a voz ha terminado con código de salida: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Estructura de datos para representar los ejercicios
    static class Ejercicio {
        String nombre;
        List<String> caracteristicas;

        Ejercicio(String nombre, List<String> caracteristicas) {
            this.nombre = nombre;
            this.caracteristicas = caracteristicas;
        }
    }

    // Base de datos de ejercicios (esto podría provenir de tu sistema o base de datos)
    static List<Ejercicio> baseDeDatosEjercicios = Arrays.asList(
            new Ejercicio("Estiramiento de brazos", Arrays.asList("Estiramiento", "Brazos")),
            new Ejercicio("Sentadillas", Arrays.asList("Piernas", "Cardio")),
            new Ejercicio("Flexiones", Arrays.asList("Pecho", "Tríceps"))
    // ... otros ejercicios
    );

    // Perfil de usuario (simulado, debes adaptarlo según tus datos reales)
    static List<String> ejerciciosRealizadosPorUsuario = Arrays.asList(
            "Estiramiento de brazos",
            "Sentadillas"
    // ... otros ejercicios realizados por el usuario
    );

    // Función para calcular la similitud coseno entre dos listas de características
    private static double similitudCoseno(List<String> a, List<String> b) {
        Set<String> conjuntoA = new HashSet<>(a);
        Set<String> conjuntoB = new HashSet<>(b);

        // Intersección de conjuntos
        conjuntoA.retainAll(conjuntoB);

        // Magnitudes de los vectores
        double magnitudA = Math.sqrt(a.size());
        double magnitudB = Math.sqrt(b.size());

        // Cálculo de la similitud coseno
        return conjuntoA.size() / (magnitudA * magnitudB);
    }

    // Función para recomendar ejercicios al usuario
    public static List<Ejercicio> recomendarEjercicios() {
        List<Ejercicio> recomendaciones = new ArrayList<>();

        for (Ejercicio ejercicio : baseDeDatosEjercicios) {
            // Calcular similitud con ejercicios realizados por el usuario
            double similitud = similitudCoseno(ejercicio.caracteristicas, ejerciciosRealizadosPorUsuario);

            // Establecer un umbral de similitud (ajustable según tus necesidades)
            double umbralSimilitud = 0.3;

            // Si la similitud supera el umbral, agregar a las recomendaciones
            if (similitud > umbralSimilitud) {
                recomendaciones.add(ejercicio);
            }
        }

        return recomendaciones;
    }

    public static void main(String[] args) {
        // Obtener recomendaciones
        List<Ejercicio> recomendaciones = recomendarEjercicios();

        // Imprimir las recomendaciones
        System.out.println("Recomendaciones de Ejercicios:");
        for (Ejercicio ejercicio : recomendaciones) {
            System.out.println("- " + ejercicio.nombre);
        }
    }
}
