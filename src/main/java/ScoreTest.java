import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class ScoreTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        BufferedImage lastImage = Camera.takePicture("test.jpg");

        while (true) {
            BufferedImage curImage = Camera.takePicture("test.jpg");

            System.out.println(ImageProcessing.changedColor(lastImage, curImage));

            lastImage = curImage;
        }

		/*
		BufferedImage img;
		try {
			for(int i = 1; i<5;i++){
				System.out.println("picture: "+i);
				img = ImageIO.read(new File("picture/pic"+i+".jpg"));
				ImageProcessing.getScore(img);
				System.out.println("end");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    }
}
