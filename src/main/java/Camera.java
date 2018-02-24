import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Camera {
    public static void startCamera() {
        try {
            ProcessBuilder camera = new ProcessBuilder("raspistill", "-t", "0", "-w", "1640", "-h", "922", "-q", "10", "-s", "-o", "temp.jpg");
            Process cameraProcess = camera.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image takePreparedPicture() {
        ProcessBuilder signal = new ProcessBuilder("kill", "");

        return null;
    }

    /**
     * Takes a picture with the given filename and returns an Image object of the image taken.
     *
     * @param filename the file name of the new image
     * @return an Image object of the image taken
     */
    public static BufferedImage takePicture(String filename) {
        try {
            ProcessBuilder camera = new ProcessBuilder("raspistill", "-t", "2", "-o", filename);
            Process cameraProcess = camera.start();
            cameraProcess.waitFor();

            File imageFile = new File(filename);
            return ImageIO.read(imageFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
