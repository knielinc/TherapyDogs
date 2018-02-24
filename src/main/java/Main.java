import com.pi4j.io.gpio.*;

import java.awt.image.BufferedImage;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        UltraSens ultraSens = new UltraSens();
        Controller cont = new Controller(ultraSens);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FlightController.setArm(1000);
            FlightController.setRunning(false);
        }));

        // Start flight controller
        new Thread(new FlightController()).start();

        FlightController.setArm(1000);
        FlightController.setThrust(1000);
        FlightController.setPitch(1500);
        FlightController.setRoll(1500);
        FlightController.setYaw(1500);

        Thread.sleep(2000);

        FlightController.setArm(2000);
        FlightController.setThrust(1000);
        FlightController.setPitch(1500);
        FlightController.setRoll(1500);
        FlightController.setYaw(1500);

        Thread.sleep(2000);

        Camera.startCamera();

        System.out.println("I am ready!");

        BufferedImage lastImage = Camera.takePreparedPicture();

        while (true) {
            BufferedImage curImage = Camera.takePreparedPicture();

            if (ImageProcessing.changedColor(lastImage, curImage)) {
                break;
            }

            lastImage = curImage;
        }

        Camera.stopCamera();

        while (true) {
            cont.startFlight();
        }
    }
}
