import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("TheRapyDrone - A Rapy Dogs Product");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FlightController.setArm(1000);
            FlightController.setRunning(false);
        }));

        // Start flight controller
        new Thread(new FlightController()).start();

        if (args.length > 0 && args[0].equals("--motorTest")) {
            FlightController.setArm(1000);
            FlightController.setThrust(1200);
            FlightController.setPitch(1400);
            FlightController.setRoll(1600);
            FlightController.setYaw(1800);

            while (true) { Thread.sleep(1000); }
        } else {
            UltraSens ultraSens = new UltraSens();
            Controller cont = new Controller(ultraSens);

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

            logger.info("Drone ready for start!");

            BufferedImage lastImage = Camera.takePreparedPicture();

            while (true) {
                BufferedImage curImage = Camera.takePreparedPicture();

                if (ImageProcessing.changedColor(lastImage, curImage)) {
                    break;
                }

                lastImage = curImage;
            }

            Camera.stopCamera();

            logger.info("Drone starts");
            while (true) {
                cont.startFlight();
            }
        }
    }
}
