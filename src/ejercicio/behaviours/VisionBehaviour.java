//package ejercicio.behaviours;
//
//import org.opencv.core.Point;
//import org.opencv.core.Scalar;
//import org.opencv.core.Core;
//import org.opencv.core.MatOfByte;
//import org.opencv.core.CvType;
//import org.opencv.core.MatOfFloat;
//import org.opencv.highgui.HighGui;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfRect;
//import org.opencv.core.Rect;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.objdetect.CascadeClassifier;
//
//import jade.core.behaviours.Behaviour;
//import jade.core.AID;
//import jade.lang.acl.ACLMessage;
//import org.opencv.videoio.VideoCapture;
//
//public class VisionBehaviour extends Behaviour {
//
//    private CascadeClassifier fullBodyCascade;
//    private CascadeClassifier fistCascade;
//    private MatOfRect fullBodyDetections = new MatOfRect();
//    private MatOfRect fistDetections = new MatOfRect();
//    private Mat frame;  // Asegúrate de actualizar este frame con la imagen actual de la cámara
//
//    private long startTime = System.currentTimeMillis();
//
//    public VisionBehaviour(CascadeClassifier fullBodyCascade, CascadeClassifier fistCascade, Mat frame) {
//        this.fullBodyCascade = fullBodyCascade;
//        this.fistCascade = fistCascade;
//        this.frame = frame;
//    }
//
//    public void action() {
//        // Realizar la detección de cuerpos completos
//        detectFullBody(frame);
//
//        // Realizar la detección de manos
//        detectHands(frame);
//
//        // Enviar los resultados al Agente de Retroalimentación
//        sendResultsToFeedbackAgent(fullBodyDetections, fistDetections);
//    }
//
//    public boolean done() {
//        // Define la lógica de finalización del comportamiento.
//        // Devuelve true cuando el comportamiento debe finalizar.
//        // De lo contrario, devuelve false para que el comportamiento se ejecute nuevamente.
//        long currentTime = System.currentTimeMillis();
//        long elapsedTime = currentTime - startTime;
//
//        // Finaliza el comportamiento después de 3 minutos (180,000 milisegundos)
//        return elapsedTime >= 180000;
//    }
//
//    // Método para capturar imagen de la cámara (reemplazar con tu lógica de captura)
//    private Mat captureFrame() {
//        // Implementa tu lógica para capturar la imagen de la cámara aquí
//        // Puedes utilizar OpenCV para acceder a la cámara, por ejemplo:
//        VideoCapture capture = new VideoCapture(0);
//            if (capture.isOpened()) {
//                capture.read(frame);
//            } else {
//                System.err.println("Error al abrir la cámara.");
//            }
//        
//
//        return frame;
//    }
//
//    // Método para realizar la detección de cuerpos completos
//    private void detectFullBody(Mat frame) {
//        // Convertir la imagen a escala de grises (puede necesitar ajustes según tu escenario)
//        Mat grayFrame = new Mat();
//        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
//
//        // Realizar la detección de cuerpos completos
//        fullBodyCascade.detectMultiScale(grayFrame, fullBodyDetections);
//
//        // Dibujar rectángulos alrededor de las detecciones en la imagen original
//        Rect[] fullBodyArray = fullBodyDetections.toArray();
//        for (Rect rect : fullBodyArray) {
//            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
//                    new Point(rect.x + rect.width, rect.y + rect.height),
//                    new Scalar(0, 255, 0), 2);  // Color verde, grosor del rectángulo 2
//        }
//
//        // Puedes realizar más procesamiento aquí según tus necesidades
//    }
//
//    // Método para realizar la detección de manos
//    private void detectHands(Mat frame) {
//        // Convertir la imagen a escala de grises (puede necesitar ajustes según tu escenario)
//        Mat grayFrame = new Mat();
//        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
//
//        // Realizar la detección de manos
//        fistCascade.detectMultiScale(grayFrame, fistDetections);
//
//        // Dibujar rectángulos alrededor de las manos detectadas en la imagen original
//        Rect[] fistArray = fistDetections.toArray();
//        for (Rect rect : fistArray) {
//            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
//                    new Point(rect.x + rect.width, rect.y + rect.height),
//                    new Scalar(255, 0, 0), 2);  // Color azul, grosor del rectángulo 2
//        }
//
//        // Puedes realizar más procesamiento aquí según tus necesidades
//    }
//
//    // Método para enviar los resultados al Agente de Retroalimentación
//    private void sendResultsToFeedbackAgent(MatOfRect fullBodyDetections, MatOfRect fistDetections) {
//        // Crea un mensaje ACL para enviar los resultados al Agente de Retroalimentación
//        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
//        mensaje.setContent("Detalles de detección");
//
//        // Adjunta los datos de detección al mensaje
//        appendDetectionDataToMessage(mensaje, "FullBody", fullBodyDetections);
//        appendDetectionDataToMessage(mensaje, "Fist", fistDetections);
//
//        // Envía el mensaje al Agente de Retroalimentación
//        mensaje.addReceiver(new AID("AgenteRetroalimentacion", AID.ISLOCALNAME));
//        send(mensaje);
//    }
//
//    // Método para adjuntar datos de detección al mensaje
//    private void appendDetectionDataToMessage(ACLMessage mensaje, String tipoDeteccion, MatOfRect detections) {
//        Rect[] detectionsArray = detections.toArray();
//        StringBuilder datos = new StringBuilder();
//
//        // Formatea los datos de detección como coordenadas (x, y, ancho, alto)
//        for (Rect rect : detectionsArray) {
//            datos.append(String.format("(%d, %d, %d, %d) ", rect.x, rect.y, rect.width, rect.height));
//        }
//
//        // Adjunta los datos al mensaje
//        mensaje.addUserDefinedParameter(tipoDeteccion, datos.toString());
//    }
//}
