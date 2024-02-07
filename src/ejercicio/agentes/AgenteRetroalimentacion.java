package ejercicio.agentes;

import ejercicio.agentes.SistemaRecomendacion;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.List;
//import ejercicio.agentes.SistemaRecomendacion.Ejercicio;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;

public class AgenteRetroalimentacion extends Agent {

    private boolean recomendacionesRealizadas = false;
    // Configuración de la codificación

    // Ruta del archivo CSV
    String csvFilePath = "resources/rutinas_entrenamiento.csv";

    public void setup() {

        super.setup();  // Asegúrate de llamar a super.setup() si es necesario
        System.setProperty("file.encoding", "UTF-8");
        // Resto del código de setup aquí
        speakText("¡Hola! Estoy listo para ayudarte.");
        if (getAID() != null) {
            System.out.println("Agente de Retroalimentación iniciado: " + getAID().getName());
        } else {
            System.out.println("Error: El AID es nulo.");
        }
        addBehaviour(new FeedbackBehaviour());
    }

    private static double similitudCoseno(List<String> a, List<String> b) {
        Set<String> conjuntoA = new HashSet<>(a);
        Set<String> conjuntoB = new HashSet<>(b);

        conjuntoA.retainAll(conjuntoB);

        double magnitudA = Math.sqrt(a.size());
        double magnitudB = Math.sqrt(b.size());

        return conjuntoA.size() / (magnitudA * magnitudB);
    }

    // Función para hablar el texto proporcionado
    private void speakText(String textToRead) {
        try {
            // Especifica la ruta completa al ejecutable espeak
            String espeakPath = "C:\\Program Files (x86)\\eSpeak\\command_line\\espeak.exe";   // Reemplaza con la ubicación real de espeak.exe en tu sistema

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

    private class FeedbackBehaviour extends CyclicBehaviour {

        private String ultimaInfoCara = "";
        private String ultimaInfoCuerpo = "";

        public void action() {
            ACLMessage mensaje = receive();

            if (mensaje != null) {
                String contenido = mensaje.getContent();
                System.out.println("Agente de Retroalimentación recibió: " + contenido);

                System.out.println("Detalles de detección:");

                String[] tiposDeteccion = {"FullBody", "Fist", "Face"};
                for (String tipo : tiposDeteccion) {
                    if (mensaje.getUserDefinedParameter(tipo) != null) {
                        String datosDeteccion = mensaje.getUserDefinedParameter(tipo);
                        System.out.println(tipo + ": " + datosDeteccion);
                    }
                }

                int numCarasDetectadas = mensaje.getUserDefinedParameter("Face") != null
                        ? mensaje.getUserDefinedParameter("Face").split("\\s+").length
                        : 0;

                String nuevaInfoCara = mensaje.getUserDefinedParameter("Face");

                if (!nuevaInfoCara.equals(ultimaInfoCara)) {
                    ultimaInfoCara = nuevaInfoCara;

                    String mensajeRetroalimentacion = "";

                    if (numCarasDetectadas == 0) {
                        mensajeRetroalimentacion = "No se detecta a nadie. ¿Puedes acercarte más a la cámara?";
                        speakText(mensajeRetroalimentacion);
                    } else if (numCarasDetectadas == 1) {
                        mensajeRetroalimentacion = "¡Hola!. ¿Cómo estás?";
                        //speakText(mensajeRetroalimentacion);
                    } else {
                        mensajeRetroalimentacion = "¡Hola! Se detectaron varias caras. ¡Sonríe!";
                    }

                    System.out.println("Mensaje de retroalimentación: " + mensajeRetroalimentacion);

                    sendResponseToVisionAgent(mensaje);
                } else {
                    System.out.println("La cara detectada sigue siendo la misma.");
                }

                if (mensaje.getUserDefinedParameter("Face") == null) {
                    String mensajeDespedida = "No se detecta a nadie. ¿Puedes acercarte más a la cámara?";
                    speakText(mensajeDespedida);
                    System.out.println("Mensaje de despedida: " + mensajeDespedida);
                }
///BODY
                int numCuerposDetectados = mensaje.getUserDefinedParameter("FullBody") != null
                        ? mensaje.getUserDefinedParameter("FullBody").split("\\s+").length
                        : 0;

                String nuevaInfoCuerpo = mensaje.getUserDefinedParameter("FullBody");

                if (!nuevaInfoCuerpo.equals(ultimaInfoCuerpo)) {
                    ultimaInfoCuerpo = nuevaInfoCuerpo;

                    String mensajeRetroalimentacion = "";

                    if (numCuerposDetectados == 0) {
                        mensajeRetroalimentacion = "No se detecta a nadie. ¿Puedes aparecer completamente en la cámara?";
                        speakText(mensajeRetroalimentacion);
                    } else if (numCuerposDetectados == 1) {
                        mensajeRetroalimentacion = "¡Hola! ¿Cómo estás? Asegúrate de estar completamente visible en la cámara.";
                        List<Ejercicio> listaEjercicios = obtenerListaEjerciciosDesdeCSV(csvFilePath);

//        System.out.println(listaEjercicios);
                        RecomendadorEjercicios recomendador = new RecomendadorEjercicios(listaEjercicios);

                        // Ejemplo de recomendación: tipo deseado = "Piernas", dificultad deseada = "Moderado"
                        List<Ejercicio> recomendaciones = recomendador.recomendarEjercicios("Abdominales", "Moderado");

                        // Imprime las recomendaciones
                        System.out.println("Recomendaciones:");
                        if (recomendaciones.isEmpty()) {
                            System.out.println("No hay recomendaciones para los criterios especificados.");
                        } else {
                            for (Ejercicio recomendacion : recomendaciones) {
                                System.out.println(recomendacion.getNombre());
                                speakText(recomendacion.getNombre());
                            }
                        }

                        speakText(mensajeRetroalimentacion);
                    } else {
                        mensajeRetroalimentacion = "¡Hola! Se detectaron varias personas. ¡Sonríe!";
                    }

                    System.out.println("Mensaje de retroalimentación: " + mensajeRetroalimentacion);

                    // Enviar respuesta al agente de visión
                    sendResponseToVisionAgent(mensaje);
                } else {
                    System.out.println("El cuerpo detectado sigue siendo el mismo.");
                }

                if (mensaje.getUserDefinedParameter("FullBody") == null) {
                    String mensajeDespedida = "No se detecta a nadie. ¿Puedes aparecer completamente en la cámara?";
                    speakText(mensajeDespedida);
                    System.out.println("Mensaje de despedida: " + mensajeDespedida);
                }
                if (mensaje.getUserDefinedParameter("FullBody") == null) {
                    String mensajeDespedida = "No se detecta a nadie. ¿Puedes aparecer completamente en la cámara?";
                    speakText(mensajeDespedida);
                    System.out.println("Mensaje de despedida: " + mensajeDespedida);
                }

            } else {
                block();
            }
        }
// Método para convertir los datos del CSV a una lista de Ejercicio

        private static List<Ejercicio> obtenerListaEjerciciosDesdeCSV(String csvFilePath) {
            List<Ejercicio> listaEjercicios = new ArrayList<>();

            try ( BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
                // Omitir la primera fila que contiene los encabezados
                reader.readLine();

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(";");

                    Ejercicio ejercicio = new Ejercicio();
                    ejercicio.setId(Integer.parseInt(row[0]));
                    ejercicio.setNombre(row[1]);
                    ejercicio.setTipo(row[2]);
                    ejercicio.setDuracion(row[3]);
                    ejercicio.setRepeticiones(row[4]);
                    ejercicio.setSeries(row[5]);
                    ejercicio.setDificultad(row[6]);
                    ejercicio.setDescripcion(row[7]);
                    ejercicio.setInstrucciones(row[8]);

                    listaEjercicios.add(ejercicio);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return listaEjercicios;
        }

        private void sendResponseToVisionAgent(ACLMessage originalMessage) {
            ACLMessage respuesta = originalMessage.createReply();
            respuesta.setPerformative(ACLMessage.INFORM);
            respuesta.setContent("Retroalimentación procesada con éxito.");
            respuesta.addReceiver(originalMessage.getSender());
            send(respuesta);
        }

    }
}
