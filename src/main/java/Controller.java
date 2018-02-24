public class Controller {
    private int roomDistance = 250;
    private int rightDistance = 120;
    //TODO Implement necessary libraries/dependencies to control the flight controller
    
    public Controller(UltraSens sensor){
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

    public void set_yaw(int value){
        //TODO IMPLEMENT
    }

    public void set_pitch(int value){
        //TODO IMPLEMENT
    }

    public void set_roll(int value){
        //TODO IMPLEMENT
    }

    public void set_thrust(int value){
        //TODO IMPLEMENT
    }

    public void startFlight(){

        

        while(inflight) {
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
    
    private void liftOff(){

        stabilizeHeight();
        stabilizeCenterFontBack();
        stabilizeRight();
        
    }

    private void stabilizeHeight(){

        int currHeight = sensorData[0][0];

        if (currHeight < groundThresh){
            set_thrust(thrustHigh);
        } else if (currHeight > groundThresh + groundDistanceVariance){
            set_thrust(thrustLow);
        } else {
            set_thrust(thrustRest);
        }
    }

    private void stabilizeCenterFontBack(){
        int currBack = sensorData[3][0];
        int currFront = sensorData[1][0];

        int frontBackThrust = (((currFront - currBack) + roomDistance) / (2*roomDistance)) * (motorHigh - motorLow) + motorLow;

        set_pitch(frontBackThrust);

    }

    private void stabilizeRight(){
        int currRight = sensorData[2][0];

        int sideThrust = (((currRight - rightDistance) + roomDistance/2) / (roomDistance)) * (motorHigh - motorLow) + motorLow;

        set_roll(sideThrust);

    }
    
    private boolean isCentered(){
        //TODO implement
        return true;
    }
    
    
    
}
