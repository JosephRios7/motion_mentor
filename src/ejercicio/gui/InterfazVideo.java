/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejercicio.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.CvType;
import org.opencv.core.CvType.*;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.CvType;
import org.opencv.core.CvType.*;
//import org.opencv.core.CvType.CvTypeException;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.MatOfRotatedRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.CvType;
import org.opencv.core.CvType.*;

public class InterfazVideo {

    private JFrame frame;
    private JPanel panel;

    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private Mat frameMat;

    public InterfazVideo() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        frame = new JFrame("Interfaz de Video");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (frameMat != null) {
                    BufferedImage image = convertMatToBufferedImage(frameMat);
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);

        capture = new VideoCapture(0); // Cambia el índice si la cámara predeterminada no es la correcta
        frameMat = new Mat();

        startCapture();
    }

    public void startCapture() {
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::updateFrame, 0, 33, TimeUnit.MILLISECONDS); // Actualiza cada ~33 ms (aprox. 30 FPS)
    }

    private void updateFrame() {
        if (capture.read(frameMat)) {
            Imgproc.cvtColor(frameMat, frameMat, Imgproc.COLOR_BGR2RGB);
            panel.repaint();
        }
    }

    private BufferedImage convertMatToBufferedImage(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            return ImageIO.read(bis);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazVideo());
    }
}
