import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageProcessing {

    private static final int threshold = 30;

    public static int getScore(BufferedImage input) {
        int score = 0;
        // TODO save width height permanently, compare input and second scales
        // if necessary
        int width = input.getWidth();
        int height = input.getHeight();
        for (int x = 0; x < width; x += 4) {
            for (int y = 0; y < height; y += 4) {
                int clr = input.getRGB(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                score -= red;
                score += green;
            }
        }
        score /= width * height / 16;
        Main.logger.debug("Score: " + score);
        return score;
    }

    public static boolean changedColor(BufferedImage first, BufferedImage second) {
        if (Math.abs(getScore(first) - getScore(second)) < threshold)
            return false;
        return true;
    }

	// return 1, 2 or 3 as position of the red machine, 1 wall, 2 middle, 3 room
	public static int getPosition(ArrayList<BufferedImage> imageList) {
		int listSize = imageList.size();
		int minDist;
		if (listSize < 10)
			minDist = listSize / 3;
		else
			minDist = listSize / 4;
		int[] scoreList = new int[listSize];
		for (int i = 0; i < listSize; i++) {
			scoreList[i] = getScore(imageList.get(i));
		}

		int minTemp = 256;
		int maxTemp = -256;
		int errorIndex = 0;
		int functioningIndex1 = 0;
		int functioningIndex2 = 0;

		for (int i = 0; i < listSize; i++) {
			if (scoreList[i] < minTemp) {
				minTemp = scoreList[i];
				errorIndex = i;
			}

			if (scoreList[i] > maxTemp) {
				maxTemp = scoreList[i];
				functioningIndex1 = i;
			}
		}

		maxTemp = -256;
		for (int i = 0; i < listSize; i++) {
			if (Math.abs(i - functioningIndex1) >= minDist
					&& scoreList[i] > maxTemp) {
				maxTemp = scoreList[i];
				functioningIndex2 = i;
			}
		}
		// System.out.println("Error Index: " + errorIndex + " First Index: "
		// + functioningIndex1 + " Second Index: " + functioningIndex2);

		if (errorIndex < functioningIndex1 && errorIndex < functioningIndex2)
			return 0;
		if (errorIndex > functioningIndex1 && errorIndex < functioningIndex2
				|| errorIndex < functioningIndex1
				&& errorIndex > functioningIndex2)
			return 1;
		if (errorIndex > functioningIndex1 && errorIndex > functioningIndex2)
			return 2;

		return -1;

	}
}