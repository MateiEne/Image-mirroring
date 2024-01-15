package packWork;

public class BMP {

    private int width;
    private int height;
    private int[][] pixelMatrix;

    {
        width = 0;
        height = 0;
        pixelMatrix = new int[0][0];
    }

    public BMP(int width, int height, int[][] pixelMatrix) {
        this.width = width;
        this.height = height;
        this.pixelMatrix = pixelMatrix;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getPixelMatrix() {
        return pixelMatrix;
    }
}
