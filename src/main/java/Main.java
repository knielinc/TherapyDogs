import com.pi4j.io.gpio.*;


public class Main {
    //GPIO Pins


    public static void main(String[] args) {
        UltraSens ultraSens = new UltraSens();
        Controller cont = new Controller(ultraSens);

        cont.startFlight();
    }
}
