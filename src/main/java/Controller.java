import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

import static com.pi4j.wiringpi.Gpio.PWM_MODE_MS;

public class Controller {
    private int roomDistance = 250;
    private int rightDistance = 120;
    //TODO Implement necessary libraries/dependencies to control the flight controller

    private static GpioController gpio = GpioFactory.getInstance();

    private static final int CLOCK = 96;
    private static final int RANGE = 4000;

    static {
        Gpio.pwmSetMode(PWM_MODE_MS);
        Gpio.pwmSetClock(CLOCK);
        Gpio.pwmSetRange(RANGE);
    }

    public Controller(UltraSens sensor) {
        //TODO init if needed
        mySensor = sensor;
    }

    private UltraSens mySensor;

    private myStage currStage = myStage.LIFTOFF;

    private boolean inflight = true;

    private int[][] sensorData = new int[5][2]; // Bottom, Front, Right, Back, Left

    private int motorLow = 1400;

    private int motorRest = 1500;

    private int motorHigh = 1600;

    private int thrustHigh = 1600;

    private int thrustRest = 1500;

    private int thrustLow = 1400;

    private int groundThresh = 120;

    private int frontThresh = 120;

    private int backThresh = 120;

    private int frontBackDistanceVariance = 20;

    private int groundDistanceVariance = 20;


    public enum myStage {
        LIFTOFF,
        CENTER,
        FLYIN,
        ROTATE,
        FLYOUT,
        LANDING
    }

    /**
     * Translates a value between 1000 and 2000 to the appropriate pwm value.
     *
     * @param value the value to translate
     * @return the translated value for the pwm signal
     */
    private int translate(int value) {
        // Calculate
        int ratio = RANGE / 20;
        return (int) ((((double) value - 1000) / 1000) * ratio + ratio);
    }

    /**
     * Sets the yaw of the drone.
     *
     * @param value the yaw
     */
    public void setYaw(int value) {
        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(RaspiPin.GPIO_29);
        pwm.setPwm(translate(value));
    }

    /**
     * Sets the pitch of the drone.
     *
     * @param value the pitch
     */
    public void setPitch(int value) {
        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(RaspiPin.GPIO_28);
        pwm.setPwm(translate(value));
    }

    /**
     * Sets the roll of the drone.
     *
     * @param value the roll
     */
    public void setRoll(int value) {
        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(RaspiPin.GPIO_25);
        pwm.setPwm(translate(value));
    }

    /**
     * Sets the thrust of the drone.
     *
     * @param value the thrust
     */
    public void setThrust(int value) {
        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(RaspiPin.GPIO_27);
        pwm.setPwm(translate(value));
    }

    public void startFlight() {


        while (inflight) {
            //set thrust to start to something sensible
            switch (currStage) {
                case LIFTOFF:
                    break;
                case CENTER:
                    break;
                case FLYIN:
                    break;
                case ROTATE:
                    break;
                case FLYOUT:
                    break;
                case LANDING:
                    break;
                default:
                    break;
            }
        }
    }

    private void liftOff() {

        stabilizeHeight();
        stabilizeCenterFontBack();
        stabilizeRight();

    }

    private void stabilizeHeight() {

        int currHeight = sensorData[0][0];

        if (currHeight < groundThresh) {
            setThrust(thrustHigh);
        } else if (currHeight > groundThresh + groundDistanceVariance) {
            setThrust(thrustLow);
        } else {
            setThrust(thrustRest);
        }
    }

    private void stabilizeCenterFontBack() {
        int currBack = sensorData[3][0];
        int currFront = sensorData[1][0];

        int frontBackThrust = (((currFront - currBack) + roomDistance) / (2 * roomDistance)) * (motorHigh - motorLow) + motorLow;

        setPitch(frontBackThrust);

    }

    private void stabilizeRight() {
        int currRight = sensorData[2][0];

        int sideThrust = (((currRight - rightDistance) + roomDistance / 2) / (roomDistance)) * (motorHigh - motorLow) + motorLow;

        setRoll(sideThrust);

    }

    private boolean isCentered() {
        //TODO implement
        return true;
    }


}
