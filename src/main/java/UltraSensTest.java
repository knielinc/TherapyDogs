import java.io.IOException;

public class UltraSensTest {

    public static void main(String[] args) {
        UltraSens ultrasens = new UltraSens();
        while(true){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ultrasens.measureFront();
            ultrasens.measureLeft();
            ultrasens.measureRight();
            ultrasens.measureBack();
            ultrasens.measureBottom();
            try {
                Runtime.getRuntime().exec("clear");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
