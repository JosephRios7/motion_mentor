package ejercicio.agentes;
import ejercicio.models.Ejercicio;
import ejercicio.utilidades.RecomendadorEjercicios;
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

public class FeedbackAgent extends Agent {

    private boolean recomendacionesRealizadas = false;
    private boolean esperando = false;
     private int contadorSaludo = 0;
    
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
            //int exitCode = process.exitValue();
            //System.out.println("El proceso de texto a voz ha terminado con código de salida: " + exitCode);
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

                // Analizar el contenido del mensaje para extraer el tipo y la dificultad del ejercicio
                String tipo = null;
                String dificultad = null;
                String[] partes = contenido.split(";");
                for (String parte : partes) {
                    String[] keyValue = parte.trim().split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        if (key.equalsIgnoreCase("Tipo")) {
                            tipo = value;
                        } else if (key.equalsIgnoreCase("Dificultad")) {
                            dificultad = value;
                        }
                    }
                }

                if (tipo != null && dificultad != null) {
                    speakText("Para el tipo " + tipo + " y la dificultad " + dificultad + "  eleccionados");
                    // Procesar la recomendación de ejercicio
                    List<Ejercicio> listaEjercicios = obtenerListaEjerciciosDesdeCSV(csvFilePath);
                    RecomendadorEjercicios recomendador = new RecomendadorEjercicios(listaEjercicios);
                    List<Ejercicio> recomendaciones = recomendador.recomendarEjercicios(tipo, dificultad);

                    // Imprimir y enviar las recomendaciones
                    System.out.println("Recomendaciones:" + recomendaciones);
                    if (recomendaciones.size() != 0) {
                        speakText("Puedo recomendarte los siguientes ejercicios");
                        for (Ejercicio recomendacion : recomendaciones) {
                            System.out.println(recomendacion.getNombre());
                            speakText(recomendacion.getNombre() + ": " + recomendacion.getDescripcion());

                        }
                    } else {
                        speakText("No puedo encontrar algo para recomendarte");
                    }
                }

                // Procesar la información sobre el número de caras y cuerpos detectados
                int numCarasDetectadas = 0;
                int numCuerposDetectados = 0;
                try {
                    numCarasDetectadas = Integer.parseInt(mensaje.getUserDefinedParameter("NumFaces"));
            numCuerposDetectados = Integer.parseInt(mensaje.getUserDefinedParameter("NumFullBodies"));
            
               } catch (Exception e) {
                   System.out.println("Error con num" + e);
                }
 
            // Realizar acciones basadas en el número de caras y cuerpos detectados
            if (numCarasDetectadas == 0) {
                if (contadorSaludo > 0) {
                    String mensajeRetroalimentacion = "No se detecta a nadie. ¿Puedes acercarte más a la cámara?";
                    speakText(mensajeRetroalimentacion);
                    contadorSaludo = 0; // Reiniciar el contador de saludo
                }
            } else if (numCarasDetectadas == 1) {
                if (contadorSaludo == 0) {
                    String mensajeRetroalimentacion = "¡Hola! ¿Cómo estás?";
                    speakText(mensajeRetroalimentacion);
                    contadorSaludo++; // Incrementar el contador de saludo
                }
                if (numCuerposDetectados == 1) {
                    String mensajeRetroalimentacion = "Estás listo para el entrenamiento?";
                    speakText(mensajeRetroalimentacion);
                }
            } else if (numCarasDetectadas > 1) {
                if (contadorSaludo == 0) {
                    String mensajeRetroalimentacion = "¡Hola! Se detectaron varias personas. ¡Sonríe!";
                    speakText(mensajeRetroalimentacion);
                    contadorSaludo++; // Incrementar el contador de saludo
                }
                if (numCuerposDetectados > 1) {
                    String mensajeRetroalimentacion = "Están listos para el entrenamiento?";
                    speakText(mensajeRetroalimentacion);
                }
            }

        } else {
            block();
        }
            
        }
        // Método para pausar la ejecución del comportamiento durante un tiempo determinado
    private void esperar(int milisegundos) {
        esperando = true;
        myAgent.doWait(milisegundos);
        esperando = false;
    }

//
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
