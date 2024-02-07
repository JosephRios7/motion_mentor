package ejercicio.agentes;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat6;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import org.opencv.highgui.HighGui;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.MatOfInt;
import org.opencv.core.CvType;
import org.opencv.core.MatOfFloat;

import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import jade.core.behaviours.Behaviour;
import jade.wrapper.StaleProxyException;

// Importa la clase HighGui para mostrar la interfaz gráfica
import org.opencv.highgui.HighGui;

//import ejercicio.behaviours.VisionBehaviour;
public class AgenteVision extends Agent {

    static {
        // Carga la biblioteca nativa de OpenCV al cargar la clase
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private CascadeClassifier fullBodyCascade;
    private CascadeClassifier fistCascade;
    private MatOfRect fullBodyDetections;
    private MatOfRect fistDetections;
    private CascadeClassifier faceCascade;
    private MatOfRect faceDetections;

    public void setup() {
        try {
//            System.out.println("Ruta del clasificador de cuerpo completo: " + getClass().getResource("haarcascade_fullbody.xml"));
//            System.out.println("Ruta del clasificador de manos: " + getClass().getResource("hand.xml"));
//            System.out.println("Ruta del clasificador de caras: " + getClass().getResource("haarcascade_frontalface_default.xml"));

// Reemplaza las líneas existentes en tu código con las anteriores
//            fullBodyCascade = new CascadeClassifier(getClass().getResource("haarcascade_fullbody.xml").getPath());
//            fistCascade = new CascadeClassifier(getClass().getResource("hand.xml").getPath());
//            faceCascade = new CascadeClassifier(getClass().getResource("haarcascade_frontalface_default.xml").getPath());
            // Cargar clasificadores
            fullBodyCascade = new CascadeClassifier("haarcascade_fullbody.xml");
            fistCascade = new CascadeClassifier("hand.xml");
            faceCascade = new CascadeClassifier("haarcascade_frontalface_default.xml");
            if (faceCascade.empty()) {
                System.err.println("Error: Clasificador de cuerpo completo no cargado correctamente.");
            }
            fullBodyDetections = new MatOfRect();
            fistDetections = new MatOfRect();
            faceDetections = new MatOfRect();

            // Iniciar la lógica principal del agente
            addBehaviour(new VisionBehaviour());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error setup" + e);
        }
    }

    protected void takeDown() {
        // Liberar recursos al finalizar
        fullBodyCascade = null;
        fistCascade = null;
        fullBodyDetections = null;
        fistDetections = null;
    }

    private class VisionBehaviour extends Behaviour {
// Añade un atributo para almacenar el nombre de la ventana

        private final String WINDOW_NAME = "Video Capturado";

        public void action() {
            // Capturar imagen de la cámara (reemplazar con tu lógica de captura)
            Mat frame = captureFrame();

            // Verificar si la imagen capturada es válida
        if (!frame.empty()) {
            // Realizar detección de cuerpo completo
            detectFullBody(frame);

            // Realizar detección de manos
            detectHands(frame);

            // Realizar detección de caras
            detectFaces(frame);

            // Enviar resultados al Agente de Retroalimentación
            sendResultsToFeedbackAgent(fullBodyDetections, fistDetections, faceDetections);

            // Mostrar el video en una ventana
            displayVideoFrame(frame);
        } else {
            System.err.println("Error: La imagen capturada es nula o vacía.");
        }
        }

        public boolean done() {
            return false; // Continuar ejecutando indefinidamente
        }

        // Método para mostrar el video en una ventana
        private void displayVideoFrame(Mat frame) {
            HighGui.imshow(WINDOW_NAME, frame);  // Muestra la imagen en una ventana con el nombre dado
            HighGui.waitKey(1);  // Espera 1 milisegundo (necesario para que la ventana se actualice)
            
        }
        // Método para liberar recursos al finalizar
    protected void takeDown() {
        // Liberar recursos al finalizar
        fullBodyCascade = null;
        fistCascade = null;
        fullBodyDetections = null;
        fistDetections = null;

        // Cierra la ventana de video al finalizar
        HighGui.destroyAllWindows();
    }

    }

    // Método para capturar imagen de la cámara (reemplazar con tu lógica de captura)
    private Mat captureFrame() {
        //System.out.println("capturando img");
        VideoCapture capture = new VideoCapture(0);  // 0 indica la cámara predeterminada, ajusta según sea necesario
        Mat frame = new Mat();

        if (capture.isOpened()) {
            //System.out.println("capture open");
            capture.read(frame);
            capture.release();  // Liberar la cámara después de leer el fotograma
        } else {
            System.err.println("Error al abrir la cámara.");
        }

        return frame;
    }
    // Método para detectar caras

    private void detectFaces(Mat frame) {
        //System.out.println("Iniciando detección de caras...");

        // Convertir la imagen a escala de grises (puede necesitar ajustes según tu escenario)
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        // Realizar la detección de caras
        faceCascade.detectMultiScale(grayFrame, faceDetections);

        // Imprimir información sobre la cantidad de caras detectadas
//        System.out.println("Número de caras detectadas: " + faceDetections.toArray().length);

        // Dibujar rectángulos alrededor de las caras detectadas en la imagen original
        Rect[] faceArray = faceDetections.toArray();
        for (Rect rect : faceArray) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);  // Color rojo, grosor del rectángulo 2
        }

        // Puedes realizar más procesamiento aquí según tus necesidades
    }

    // Método para detectar cuerpos completos
    private void detectFullBody(Mat frame) {
        // Convertir la imagen a escala de grises (puede necesitar ajustes según tu escenario)
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        // Realizar la detección de cuerpos completos
        fullBodyCascade.detectMultiScale(grayFrame, fullBodyDetections);

        // Dibujar rectángulos alrededor de las detecciones en la imagen original
        Rect[] fullBodyArray = fullBodyDetections.toArray();
        for (Rect rect : fullBodyArray) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), 2);  // Color verde, grosor del rectángulo 2
        }

        // Puedes realizar más procesamiento aquí según tus necesidades
    }

// Método para detectar manos
    private void detectHands(Mat frame) {
        // Convertir la imagen a escala de grises (puede necesitar ajustes según tu escenario)
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        // Realizar la detección de manos
        fistCascade.detectMultiScale(grayFrame, fistDetections);

        // Dibujar rectángulos alrededor de las manos detectadas en la imagen original
        Rect[] fistArray = fistDetections.toArray();
        for (Rect rect : fistArray) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(255, 0, 0), 2);  // Color azul, grosor del rectángulo 2
        }

        // Puedes realizar más procesamiento aquí según tus necesidades
    }

    // Método para enviar los resultados al Agente de Retroalimentación
    private void sendResultsToFeedbackAgent(MatOfRect fullBodyDetections, MatOfRect fistDetections, MatOfRect faceDetections) {
        // Crea un mensaje ACL para enviar los resultados al Agente de Retroalimentación
        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
        mensaje.setContent("Detalles de detección");

        // Adjunta los datos de detección al mensaje
        appendDetectionDataToMessage(mensaje, "FullBody", fullBodyDetections);
        appendDetectionDataToMessage(mensaje, "Fist", fistDetections);
        appendDetectionDataToMessage(mensaje, "Face", faceDetections);

        // Envía el mensaje al Agente de Retroalimentación
        mensaje.addReceiver(new AID("AgenteRetroalimentacion", AID.ISLOCALNAME));
        send(mensaje);
    }

// Método para adjuntar datos de detección al mensaje
    private void appendDetectionDataToMessage(ACLMessage mensaje, String tipoDeteccion, MatOfRect detections) {
        Rect[] detectionsArray = detections.toArray();
        StringBuilder datos = new StringBuilder();

        // Formatea los datos de detección como coordenadas (x, y, ancho, alto)
        for (Rect rect : detectionsArray) {
            datos.append(String.format("(%d, %d, %d, %d) ", rect.x, rect.y, rect.width, rect.height));
        }

        // Adjunta los datos al mensaje
        mensaje.addUserDefinedParameter(tipoDeteccion, datos.toString());
    }
}
