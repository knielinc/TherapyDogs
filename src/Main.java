import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedImage img;
		int numPics = new File("samples").listFiles().length;
		ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
		try {
			for (int i = 1; i < numPics; i++) {
				img = ImageIO.read(new File("samples/image" + i + ".jpg"));
				imageList.add(img);
			}
			int result = ImageProcessing.getPosition(imageList);
			if (result == 0)
				System.out.println("Error is in the first position (Wall)");
			if (result == 1)
				System.out.println("Error is in the second position (Middle)");
			if (result == 2)
				System.out.println("Error is in the third position (Room)");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
