/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejercicio.agentes;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

public class AgenteReconocimientoVoz {

    public static void main(String[] args) {
        try {
            // Configuración del reconocimiento de voz
            Configuration configuration = new Configuration();

            // Configurar el modelo acústico y el diccionario
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

            // Inicializar el reconocedor de voz en vivo
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);

            // Iniciar el reconocimiento en vivo
            recognizer.startRecognition(true);

            System.out.println("Escuchando... Di 'detener' para finalizar.");

            // Ciclo de reconocimiento continuo
            while (true) {
                // Obtener la hipótesis de reconocimiento
                String hypothesis = recognizer.getResult().getHypothesis();

                // Imprimir la hipótesis si hay alguna
                if (hypothesis != null && !hypothesis.isEmpty()) {
                    System.out.println("Reconocido: " + hypothesis);

                    // Puedes agregar lógica adicional según lo que quieras hacer con la hipótesis
                }

                // Verificar si se ha dicho 'detener' para finalizar el reconocimiento
                if (hypothesis != null && hypothesis.equalsIgnoreCase("detener")) {
                    break;
                }
            }

            // Detener el reconocimiento
            recognizer.stopRecognition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
