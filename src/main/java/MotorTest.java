public class MotorTest {
    public static void main(String[] args) {
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

        FlightController.setArm(2000);
        FlightController.setThrust(1000);
        FlightController.setPitch(1500);
        FlightController.setRoll(1500);
        FlightController.setYaw(1500);

        FlightController.setArm(2000);
        FlightController.setThrust(2000);
        FlightController.setPitch(1500);
        FlightController.setRoll(1500);
        FlightController.setYaw(1500);
    }
}
