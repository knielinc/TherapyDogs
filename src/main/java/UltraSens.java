import com.pi4j.io.gpio.*;

public class UltraSens {
    private static GpioPinDigitalOutput[] sensorTriggerPins = new GpioPinDigitalOutput[5];
    private static GpioPinDigitalInput[] sensorEchoPins = new GpioPinDigitalInput[5];

    final static GpioController gpio = GpioFactory.getInstance();

    static {
        //Front
        sensorTriggerPins[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00); // Trigger pin as OUTPUT
        sensorEchoPins[0] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN); // Echo pin as INPUT
        //Left
        sensorTriggerPins[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
        sensorEchoPins[1] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
        //Right
        sensorTriggerPins[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        sensorEchoPins[2] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
        //Back
        sensorTriggerPins[3] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21);
        sensorEchoPins[3] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, PinPullResistance.PULL_DOWN);
        //Bottom
        sensorTriggerPins[4] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23);
        sensorEchoPins[4] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24, PinPullResistance.PULL_DOWN);

    }

    /**
     * Measures distance with UltraSonSens
     *
     * @param sensNr index of sensor to measure with
     * @return distance measured in cm
     */
    private int measureDist(int sensNr) {
        try {
            sensorTriggerPins[sensNr].high(); // Make trigger pin HIGH
            Thread.sleep((long) 0.01);// Delay for 10 microseconds
            sensorTriggerPins[sensNr].low(); //Make trigger pin LOW

            long startTime = System.currentTimeMillis();
            while (sensorEchoPins[sensNr].isLow()) { // Wait until the ECHO pin gets HIGH
                if (System.currentTimeMillis() > startTime + 400) {
                    Main.logger.warn("Sensor " + sensNr + " is not responding!");
                    return -1;
                }
            }
            startTime = System.nanoTime(); // Store the current time to calculate ECHO pin HIGH time.
            while (sensorEchoPins[sensNr].isHigh()) { //Wait until the ECHO pin gets LOW

            }
            long endTime = System.nanoTime(); // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.

            int dist = (int) ((((endTime - startTime) / 1e3) / 2) / 29.1);
            if (dist > 400) {
                return -1;
            }

            return dist;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Prints distance measured.
     *
     * @param d   distance measured
     * @param dir direction measured at
     */
    private void print(int d, String dir) {
        Main.logger.trace("Distance " + dir + ": " + d + " cm"); //Printing out the distance in cm
    }

    /**
     * Measures and print distance of FRONT UltraSonSens
     *
     * @return distance at FRONT
     */
    public int measureFront() {
        int d = measureDist(0);
        print(d, "Front");
        return d;
    }

    /**
     * Measures and print distance of LEFT UltraSonSensor
     *
     * @return distance at LEFT
     */
    public int measureLeft() {
        int d = measureDist(1);
        print(d, "Left");
        return d;
    }

    /**
     * Measures and print distance of RIGHT UltraSonSensor
     *
     * @return distance at RIGHT
     */
    public int measureRight() {
        int d = measureDist(2);
        print(d, "Right");
        return d;
    }

    /**
     * Measures and print distance of BACK UltraSonSensor
     *
     * @return distance at BACK
     */
    public int measureBack() {
        int d = measureDist(3);
        print(d, "Back");
        return d;
    }

    /**
     * Measures and print distance of BOTTOM UltraSonSensor
     *
     * @return distance at BOTTOM
     */
    public int measureBottom() {
        int d = measureDist(4);
        print(d, "Bottom");
        return d;
    }

}
