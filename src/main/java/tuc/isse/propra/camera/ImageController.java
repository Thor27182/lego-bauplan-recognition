/*
Author:
Thore Jonathan Braun, tjb18@tu-clausthal.de
Contributors:
Last change:
2020-06-01
 */
package tuc.isse.propra.camera;

import javafx.scene.image.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Class that add a Camera to the System
 * @author Thore Jonathan Braun, Oliver Neumann
 * @version 1.3
 */
public class ImageController {

    private static Logger logger = Logger.getLogger(ImageController.class.getName());
    private final static int defaultCameraID = 0;

    /**
     * Opens an usb-webcam, take an image and close the webcam.
     *
     * @return Mat
     */
    public static Mat takePicture(){
        VideoCapture webcam = new VideoCapture();
        Mat mat = new Mat();
        webcam.open(defaultCameraID);

        if(webcam.isOpened()){
            webcam.read(mat);
        }

        webcam.release();
        return mat;
    }

    /**
     * converts an opencv Mat to an awt BufferedImage
     * @param mat openCV.Mat
     * @return BufferedImage
     */
    public static BufferedImage convertMatToBufferdImage(Mat mat){
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();

        try{
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(byteArray));
            return bufferedImage;
        }catch (IOException e){
            return null;
        }
    }

    /**
     * converts an awt BufferedImage to an awt WritableImage
     * @param bufferedImage java.awt.image.BufferedImage
     * @return WritableImage
     */
    public static WritableImage convertBufferedImageToWritableImage(BufferedImage bufferedImage){
        WritableImage writableImage;
        if (bufferedImage != null) {
            writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = writableImage.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
            return writableImage;
        }else{
            return null;
        }
    }

    /**
     * converts an opencv Mat to awt WritableImage
     * @param mat openCV.Mat
     * @return WritableImage
     */
    public static WritableImage convertMatToWritableImage(Mat mat){
        return  convertBufferedImageToWritableImage(convertMatToBufferdImage(mat));
    }

    /**
     * converts an awt Buffered Image to Javafx Image
     * @param bufferedImage java.awt.image.BufferedImage
     * @return Image
     */
    public static Image convertBufferedImageToImage(BufferedImage bufferedImage) {
        WritableImage wr = null;
        if (bufferedImage != null) {
            wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    /**
     * converts an opencv Mat to a Javafx Image
     * @param mat openCV.Mat
     * @return Image
     */
    public static Image convertMatToImage(Mat mat){
        return convertBufferedImageToImage(convertMatToBufferdImage(mat));
    }

    /**
     * converts a Javafx image to an opencv Mat
     * @param image javafx.scene.image.Image
     * @return Mat
     */
    public static Mat convertImageToMat(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];

        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);

        Mat mat = new Mat(height, width, CvType.CV_8UC4);
        mat.put(0, 0, buffer);
        return mat;
    }

    /**
     * manipulates an Opencv Mat with an Canny-Algorithmus to get edge-detection.
     * converts a Javafx Image to opencv Mat
     * @param image javafx.scene.image.Image
     * @return Mat
     */
    public static Mat edgeDetection(Image image){
        Mat transform = convertImageToMat(image);

        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();

        // convert to grayscale
        Imgproc.cvtColor(transform, grayImage, Imgproc.COLOR_BGR2GRAY);

        // reduce noise with a 3x3 kernel
        Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));

        // canny detector, with ratio of lower:upper threshold of 3:1
        Imgproc.Canny(detectedEdges, detectedEdges, 35, 105);

        return detectedEdges;
    }

    /**
     * Saves an Mat to a JPEG-File
     * @param mat openCV.Mat
     * @throws IOException e
     */
    public static void writeMatToJPEG(Mat mat) throws IOException {
        String pathname = "recognize-this.jpg";
        BufferedImage bufferedImage = convertMatToBufferdImage(mat);
        Runnable writeJPEG = new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(pathname);
                    if (bufferedImage != null) {
                        ImageIO.write(bufferedImage, "JPEG", file);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        };
        Thread thread = new Thread(writeJPEG);
        thread.start();
    }

}
