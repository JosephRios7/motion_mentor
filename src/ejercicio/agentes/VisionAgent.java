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

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Importa la clase HighGui para mostrar la interfaz gráfica
import org.opencv.highgui.HighGui;

import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VisionAgent extends Agent {

    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private CascadeClassifier fullBodyCascade;
    private CascadeClassifier faceCascade;
    private MatOfRect fullBodyDetections;
    private MatOfRect faceDetections;

    private JFrame mainFrame;
    private JPanel imagePanel;
    private JLabel imageLabel;
    private JLabel nivelLabel;
    private String dificultad;
    private JLabel tipoLabel;
    private String tipo;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void setup() {
        try {
            fullBodyCascade = new CascadeClassifier("resources/haarcascade_fullbody.xml");
            faceCascade = new CascadeClassifier("resources/haarcascade_frontalface_default.xml");
            if (faceCascade.empty()) {
                System.err.println("Error: Clasificador de cuerpo completo no cargado correctamente.");
            }
            fullBodyDetections = new MatOfRect();
            faceDetections = new MatOfRect();

            capture = new VideoCapture(0); // Inicialización de capture
            if (!capture.isOpened()) {
                System.err.println("Error: No se puede abrir la cámara.");
                return;
            }

            addBehaviour(new VisionBehaviour());
            SwingUtilities.invokeLater(this::initializeUI);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error setup" + e);
        }
    }

    private void initializeUI() {
        mainFrame = new JFrame("Video Capture Example");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imagePanel = new JPanel();
        imageLabel = new JLabel();
        imagePanel.add(imageLabel);

        nivelLabel = new JLabel("Nivel: ");
        tipoLabel = new JLabel("Tipo: ");

        JButton principianteButton = new JButton("Principiante");
        principianteButton.addActionListener(e -> {
            dificultad = "Principiante";
            nivelLabel.setText("Nivel: " + dificultad);

        });

        JButton intermedioButton = new JButton("Intermedio");
        intermedioButton.addActionListener(e -> {
            dificultad = "Intermedio";
            nivelLabel.setText("Nivel: " + dificultad);

        });

        JButton avanzadoButton = new JButton("Avanzado");
        avanzadoButton.addActionListener(e -> {
            dificultad = "Avanzado";
            nivelLabel.setText("Nivel: " + dificultad);

        });
        JButton piernasButton = new JButton("Piernas");
        piernasButton.addActionListener(e -> {
            tipo = "Piernas";
            tipoLabel.setText("Tipo: " + tipo);
            enviarMensajeTipoYDificultad(tipo, dificultad);
        });

        JButton pechoButton = new JButton("Pecho");
        pechoButton.addActionListener(e -> {
            tipo = "Pecho";
            tipoLabel.setText("Tipo: " + tipo);
            enviarMensajeTipoYDificultad(tipo, dificultad);
        });

        JButton abdominalesButton = new JButton("Abdominales");
        abdominalesButton.addActionListener(e -> {
            tipo = "Abdominales";
            tipoLabel.setText("Tipo: " + tipo);
            enviarMensajeTipoYDificultad(tipo, dificultad);
        });

        JButton tricepsButton = new JButton("Tríceps");
        tricepsButton.addActionListener(e -> {
            tipo = "Tríceps";
            tipoLabel.setText("Tipo: " + tipo);
            enviarMensajeTipoYDificultad(tipo, dificultad);
        });

        JButton cardioButton = new JButton("Cardio");
        cardioButton.addActionListener(e -> {
            tipo = "Cardio";
            tipoLabel.setText("Tipo: " + tipo);
            enviarMensajeTipoYDificultad(tipo, dificultad);
        });

        JPanel tipoPanel = new JPanel();
        tipoPanel.setLayout(new GridLayout(1, 5)); // Ajustar el diseño para que los botones estén en una fila

        tipoPanel.add(piernasButton);
        tipoPanel.add(pechoButton);
        tipoPanel.add(abdominalesButton);
        tipoPanel.add(tricepsButton);
        tipoPanel.add(cardioButton);

        JPanel nivelPanel = new JPanel();
        nivelPanel.setLayout(new GridLayout(1, 3)); // Ajustar el diseño para que los botones estén en una fila

        nivelPanel.add(principianteButton);
        nivelPanel.add(intermedioButton);
        nivelPanel.add(avanzadoButton);

        mainFrame.setLayout(new BorderLayout());

        mainFrame.add(imagePanel, BorderLayout.CENTER);
        mainFrame.add(tipoPanel, BorderLayout.SOUTH); // Alinea los botones de tipo debajo del panel de imagen
        mainFrame.add(nivelPanel, BorderLayout.NORTH); // Alinea los botones de dificultad a la derecha
        mainFrame.add(nivelLabel, BorderLayout.WEST);
        mainFrame.add(tipoLabel, BorderLayout.EAST);

        mainFrame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(new Dimension((int) (screenSize.width * 0.7), (int) (screenSize.height * 0.7)));

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        startVideoCapture();
    }

    private void startVideoCapture() {
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.err.println("Error: No se puede abrir la cámara.");
            return;
        }

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::updateFrame, 0, 33, TimeUnit.MILLISECONDS);
    }

    private static final int INTERVALO_DETECCION_MS = 4000; // Intervalo de detección en milisegundos (1 segundo)
    private long ultimoTiempoDeteccion = 0;

    private void updateFrame() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoTiempoDeteccion < INTERVALO_DETECCION_MS) {
            return; // Saltar este fotograma si el intervalo de tiempo no ha pasado aún
        }
        Mat frame = new Mat();
        capture.read(frame);

        if (!frame.empty()) {
            displayFrame(frame);
            detectFullBody(frame);
            detectFaces(frame);
            sendResultsToFeedbackAgent(fullBodyDetections, faceDetections);
        }
        ultimoTiempoDeteccion = tiempoActual; // Actualizar el tiempo de última detección

    }

    private void displayFrame(Mat frame) {
        BufferedImage bufferedImage = matToBufferedImage(frame);
        imageLabel.setIcon(new ImageIcon(bufferedImage));
        imageLabel.repaint();
    }

    private int numFacesDetected = 0;
    private int numFullBodiesDetected = 0;

    private void detectFaces(Mat frame) {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        faceCascade.detectMultiScale(grayFrame, faceDetections);

        Rect[] faceArray = faceDetections.toArray();
        numFacesDetected = faceArray.length; // Actualizar el número de caras detectadas

        for (Rect rect : faceArray) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255), 2);
        }
    }

    private void detectFullBody(Mat frame) {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        fullBodyCascade.detectMultiScale(grayFrame, fullBodyDetections);

        Rect[] fullBodyArray = fullBodyDetections.toArray();
        numFullBodiesDetected = fullBodyArray.length; // Actualizar el número de cuerpos completos detectados

        for (Rect rect : fullBodyArray) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), 2);
        }
    }

    private void enviarMensajeTipoYDificultad(String tipo, String dificultad) {
        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
        mensaje.setContent("Tipo: " + tipo + "; Dificultad: " + dificultad);
        mensaje.addReceiver(new AID("AgenteRetroalimentacion", AID.ISLOCALNAME));
        send(mensaje);
    }

//    private void sendResultsToFeedbackAgent(MatOfRect fullBodyDetections, MatOfRect faceDetections) {
//        ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
//        mensaje.setContent("Detalles de detección");
//
//        appendDetectionDataToMessage(mensaje, "FullBody", fullBodyDetections);
//        appendDetectionDataToMessage(mensaje, "Face", faceDetections);
//
//        mensaje.addReceiver(new AID("AgenteRetroalimentacion", AID.ISLOCALNAME));
//        send(mensaje);
//    }
private void sendResultsToFeedbackAgent(MatOfRect fullBodyDetections, MatOfRect faceDetections) {
    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
    mensaje.setContent("Detalles de detección");
    
    int numFacesDetected = faceDetections.toArray().length;
    int numFullBodiesDetected = fullBodyDetections.toArray().length;
//    System.out.println("num faces" + numFacesDetected);
    
    mensaje.addUserDefinedParameter("NumFaces", Integer.toString(numFacesDetected));
    mensaje.addUserDefinedParameter("NumFullBodies", Integer.toString(numFullBodiesDetected));
    
    mensaje.addReceiver(new AID("AgenteRetroalimentacion", AID.ISLOCALNAME));
    send(mensaje);
}

    private void appendDetectionDataToMessage(ACLMessage mensaje, String tipoDeteccion, MatOfRect detections) {
        Rect[] detectionsArray = detections.toArray();
        StringBuilder datos = new StringBuilder();

        for (Rect rect : detectionsArray) {
            datos.append(String.format("(%d, %d, %d, %d) ", rect.x, rect.y, rect.width, rect.height));
        }

        mensaje.addUserDefinedParameter(tipoDeteccion, datos.toString());
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int width = mat.width();
        int height = mat.height();
        int channels = mat.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        mat.get(0, 0, sourcePixels);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

    private Mat captureFrame() {
        Mat frame = new Mat();
        if (capture.isOpened()) {
            capture.read(frame);
        } else {
            System.err.println("Error: No se puede capturar el fotograma, la cámara no está abierta.");
        }
        return frame;
    }

    private class VisionBehaviour extends Behaviour {

        private final String WINDOW_NAME = "Video Capturado";

        public void action() {
            Mat frame = captureFrame();

            if (!frame.empty()) {
                detectFullBody(frame);
                detectFaces(frame);
                sendResultsToFeedbackAgent(fullBodyDetections, faceDetections);
//                displayVideoFrame(frame);
                displayFrame(frame);
            } else {
                System.err.println("Error: La imagen capturada es nula o vacía.");
            }
            // Introducir una pausa de 100 milisegundos (0.1 segundos) entre cada fotograma
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public boolean done() {
            return false;
        }

        private void displayVideoFrame(Mat frame) {
            displayFrame(frame);
        }

        protected void takeDown() {
            fullBodyCascade = null;
            faceCascade = null;
            HighGui.destroyAllWindows();
        }

    }
}
