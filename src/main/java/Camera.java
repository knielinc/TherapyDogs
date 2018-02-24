import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Camera {
    /**
     * Takes a picture with the given filename and returns an Image object of the image taken.
     *
     * @param filename the file name of the new image
     * @return an Image object of the image taken
     */
    public static BufferedImage takePicture(String filename) {
        try {
            ProcessBuilder camera = new ProcessBuilder("raspistill", "-t", "1", "-o", filename);
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
