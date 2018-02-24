import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

import static com.pi4j.wiringpi.Gpio.PWM_MODE_MS;

public class Controller {
    private double roomDistance = 250;
    private double rightDistance = 120;
    private double leftDistance = 120;
    private double leftDistanceLand = 40;
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

    private final int nrOfSamples = 20;

    private int[][] sensorData = new int[5][nrOfSamples]; // Bottom, Front, Right, Back, Left

    private long[] timeTable = new long[nrOfSamples];
    
    private double motorLow = 1400;

    private double motorRest = 1500;

    private double motorHigh = 1600;

    private double thrustHigh = 1600;

    private double thrustRest = 1500;

    private double thrustLow = 1400;

    private double groundThresh = 120;

    private double frontThresh = 120;

    private double backThresh = 120;

    private double frontBackDistanceVariance = 20;

    private double groundDistanceVariance = 20;

    private double rightPassedRoomDistance = 310;


    public enum myStage {
        LIFTOFF,
        FLYIN1,
        FLYIN2,
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


        while(inflight) {
            //propagate old values in array
            for (int i = nrOfSamples - 1; i > 0; i++){
                for(int j = 0; j < 5; j++){
                    sensorData[j][i] = sensorData[j][i-1];
                }
                timeTable[i] = timeTable[i-1];
            }

            //TODO maybe in parallel
            //TODO clip/ignore false data
            sensorData[0][0] = mySensor.measureBottom();
            sensorData[1][0] = mySensor.measureFront();
            sensorData[2][0] = mySensor.measureRight();
            sensorData[3][0] = mySensor.measureBack();
            sensorData[4][0] = mySensor.measureLeft();

            switch (currStage) {
                case LIFTOFF:
                    liftOff();
                    break;
                case FLYIN1:
                    flyIn1();
                    break;
                case FLYIN2:
                    flyIn1();
                    break;
                case ROTATE:
                    break;
                case FLYOUT:
                    flyIn1();
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

    private void flyIn1(){
        stabilizeHeight();
        stabilizeCenterFontBack();
        setRoll((int) ((motorRest-motorLow)/2 + motorLow));

    }

    private void flyIn2(){
        stabilizeHeight();
        stabilizeCenterFontBack();
        stabilizeLeft();

    }

    private void landingOrientate(){

    }

    private void stabilizeHeight() {

        int currHeight = sensorData[0][0];

        if (currHeight < groundThresh) {
            setThrust((int) thrustHigh);
        } else if (currHeight > groundThresh + groundDistanceVariance) {
            setThrust((int) thrustLow);
        } else {
            setThrust((int) thrustRest);
        }
    }

    private void stabilizeCenterFontBack() {
        int currBack = sensorData[3][0];
        int currFront = sensorData[1][0];

        //TODO CAST DOUBLE TO GET GOOD MULTIPLIER
        int frontBackThrust = (int)((((currFront - currBack) + roomDistance) / (2*roomDistance)) * (motorHigh - motorLow) + motorLow);

        setPitch(frontBackThrust);

    }

    private void stabilizeRight() {
        int currRight = sensorData[2][0];

        int sideThrust = (int)((((currRight - rightDistance) + roomDistance / 2) / (roomDistance)) * (motorHigh - motorLow) + motorLow);

        setRoll(sideThrust);

    }

    private void stabilizeLeft(){
        int currLeft = sensorData[4][0];

        int sideThrust = (int) ((((leftDistance - currLeft) + roomDistance/2) / (roomDistance)) * (motorHigh - motorLow) + motorLow);

        setRoll(sideThrust);

    }

    private void stabilizeLeftLand(){
        int currLeft = sensorData[4][0];

        int sideThrust = (int) ((((leftDistanceLand - currLeft) + roomDistance/2) / (roomDistance)) * (motorHigh - motorLow) + motorLow);

        setRoll(sideThrust);

    }

    private void stabilizeCenterFontBackLand(){
        int currBack = sensorData[3][0];
        int currFront = sensorData[1][0];

        //TODO CAST DOUBLE TO GET GOOD MULTIPLIER
        int frontBackThrust = (int) ((((currFront - currBack) + roomDistance) / (2*roomDistance)) * (motorHigh - motorLow) + motorLow);

        setPitch(frontBackThrust);

    }

    private boolean isCentered(){
        //TODO implement
        return true;
    }


}
