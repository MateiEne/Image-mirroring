package packWork;

public class ImageEditorWithMirroring  extends ImageEditor {
    @Override
    public void mirrorBMP() {
        int[][] pixelMatrix = inputImage.getPixelMatrix();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width / 2; j++) {
                int temp = pixelMatrix[i][j];
                pixelMatrix[i][j] = pixelMatrix[i][width - j - 1];
                pixelMatrix[i][width - j - 1] = temp;
            }
        }

        outputImage = new BMP(width, height, pixelMatrix);
    }
}
