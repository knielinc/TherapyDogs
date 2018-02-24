import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class Camera {
    private static long pid = 0;
    private static Process cameraProcess;

    // Kill the camera on shutdown
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(Camera::stopCamera));
    }

    /**
     * Starts the camera to be able to take prepared pictures.
     */
    public static void startCamera() {
        try {
            ProcessBuilder camera = new ProcessBuilder("raspistill", "-t", "0", "-w", "1640", "-h", "922", "-q", "10", "-s", "-o", "temp.jpg");
            cameraProcess = camera.start();

            Field f = cameraProcess.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            pid = f.getLong(cameraProcess);
            f.setAccessible(false);

            Main.logger.debug("Starting camera with process id: " + pid);

            // Give the camera some time to initialize
            Thread.sleep(2000);
        } catch (IOException | IllegalAccessException | NoSuchFieldException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the camera.
     */
    public static void stopCamera() {
        if (cameraProcess != null) {
            Main.logger.debug("Stopping camera.");
            cameraProcess.destroy();
        }
    }

    /**
     * Takes a prepared picture. For this to work, the camera has to be running.
     *
     * @return an Image object of the image taken
     */
    public static BufferedImage takePreparedPicture() {
        if (cameraProcess != null) {
            try {
                ProcessBuilder signal = new ProcessBuilder("kill", "-USR1", Long.toString(pid));
                signal.start();

                Thread.sleep(600);

                File imageFile = new File("temp.jpg");
                return ImageIO.read(imageFile);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

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
