import java.awt.image.BufferedImage;

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
        System.out.println("score: " + score);
        return score;
    }

    public static boolean changedColor(BufferedImage first, BufferedImage second) {
        if (Math.abs(getScore(first) - getScore(second)) < threshold)
            return false;
        return true;
    }

}