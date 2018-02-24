public class MotorTest {
    public static void main(String[] args) {
        UltraSens sens = new UltraSens();
        Controller controller = new Controller(sens);

        controller.setArm(1000);
    }
}
