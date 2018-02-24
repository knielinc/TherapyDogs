import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;

public class FlightController implements Runnable {
    private static GpioController gpio = GpioFactory.getInstance();

    private static boolean isRunning = true;

    private static final Pin thrustPin = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_27, "");
    private static final GpioPinPwmOutput thrustPwm = gpio.provisionSoftPwmOutputPin(thrustPin);
    private static volatile int thrust = 10;

    private static final Pin rollPin = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_25, "");
    private static final GpioPinPwmOutput rollPwm = gpio.provisionSoftPwmOutputPin(rollPin);
    private static volatile int roll = 15;

    private static final Pin pitchPin = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_28, "");
    private static final GpioPinPwmOutput pitchPwm = gpio.provisionSoftPwmOutputPin(pitchPin);
    private static volatile int pitch = 15;

    private static final Pin yawPin = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_29, "");
    private static final GpioPinPwmOutput yawPwm = gpio.provisionSoftPwmOutputPin(yawPin);
    private static volatile int yaw = 15;

    private static final Pin armPin = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_06, "");
    private static final GpioPinPwmOutput armPwm = gpio.provisionSoftPwmOutputPin(armPin);
    private static volatile int arm = 10;

    /**
     * Translates a value between 1000 and 2000 to the appropriate pwm value.
     *
     * @param value the value to translate
     * @return the translated value for the pwm signal
     */
    private static int translate(int value) {
        // Calculate
        return (int) ((double) value / 100);
    }

    public static void setThrust(int thrust) {
        Main.logger.debug("Setting thrust to: " + thrust);
        FlightController.thrust = translate(thrust);
    }

    public static void setRoll(int roll) {
        Main.logger.debug("Setting roll to: " + roll);
        FlightController.roll = translate(roll);
    }

    public static void setPitch(int pitch) {
        Main.logger.debug("Setting pitch to: " + pitch);
        FlightController.pitch = translate(pitch);
    }

    public static void setYaw(int yaw) {
        Main.logger.debug("Setting yaw to: " + yaw);
        FlightController.yaw = translate(yaw);
    }

    public static void setArm(int arm) {
        Main.logger.debug("Setting arm to: " + arm);
        FlightController.arm = translate(arm);
    }

    public static void setRunning(boolean isRunning) {
        Main.logger.debug("Stopping flight controller.");
        FlightController.isRunning = isRunning;
    }

    @Override
    public void run() {
        while (isRunning) {
            armPwm.setPwm(arm);
            thrustPwm.setPwm(thrust);
            rollPwm.setPwm(roll);
            pitchPwm.setPwm(pitch);
            yawPwm.setPwm(yaw);
        }
    }
}
