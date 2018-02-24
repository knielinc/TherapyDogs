import java.awt.image.BufferedImage;

public class ScoreTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Camera.startCamera();

        BufferedImage lastImage = Camera.takePreparedPicture();

        while (true) {
            BufferedImage curImage = Camera.takePreparedPicture();

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
