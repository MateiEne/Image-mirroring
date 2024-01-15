package packWork;

public abstract class ImageEditor implements EditorInterface {
    protected BMP inputImage;
    protected BMP outputImage;

    @Override
    public void setImage(BMP image) {
        this.inputImage = image;
    }

    @Override
    public BMP getImage() {
        return outputImage;
    }

    public abstract void mirrorBMP();
}
