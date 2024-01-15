package packWork;

public class BMP {
    private int width;
    private int height;
    private int[][] pixelMatrix;

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
