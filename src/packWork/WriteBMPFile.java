package packWork;

import java.io.FileOutputStream;
import java.io.IOException;

public class WriteBMPFile {
    public static void writeBinaryImageBMP(BMP BMPImage, String fileName) {
        int[][] table = BMPImage.getPixelMatrix();

        DataConversion dataConversion = new DataConversion();
        try (FileOutputStream binaryPrinter = new FileOutputStream(fileName)) {

            byte[] bmpFH = {'B', 'M', 0, 0, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0};
            int bytesPerPx = 3;
            int paddingCount = (4 - (BMPImage.getWidth() * bytesPerPx) % 4) % 4;
            int fileSize = 54 + BMPImage.getHeight() * (bytesPerPx * BMPImage.getWidth() + paddingCount);
            dataConversion.intToByteArray(bmpFH, 2, fileSize, 4);
            binaryPrinter.write(bmpFH);

            byte[] bmpIH = new byte[40];
            dataConversion.intToByteArray(bmpIH, 0, 40, 4);
            dataConversion.intToByteArray(bmpIH, 4, BMPImage.getWidth(), 4);
            dataConversion.intToByteArray(bmpIH, 8, BMPImage.getHeight(), 4);
            dataConversion.intToByteArray(bmpIH, 12, 2, 2);
            dataConversion.intToByteArray(bmpIH, 14, 24, 2);
            binaryPrinter.write(bmpIH);

            byte[] bmpPad = new byte[3];
            byte[] acc = new byte[bytesPerPx];
            for (int y = BMPImage.getHeight() - 1; y >= 0; y--) {
                for (int x = 0; x < BMPImage.getWidth(); x++) {
                    dataConversion.intToByteArray(acc, 0, table[y][x], bytesPerPx);
                    binaryPrinter.write(acc);
                }
                binaryPrinter.write(bmpPad, 0, paddingCount);
            }
        } catch (IOException e) {
            System.out.println("Unsuccessful writing BMP file");
        }
    }

    public static byte[] writeBinaryImageBMP(BMP image) {
        int[][] table = image.getPixelMatrix();

        int bytesPerPx = 3;
        int paddingCount = (4 - (image.getWidth() * bytesPerPx) % 4) % 4;
        int fileSize = 54 + image.getHeight() * (bytesPerPx * image.getWidth() + paddingCount);

        byte[] bmpData = new byte[fileSize];
        int bmpDataOffset = 0;

        DataConversion dataConversion = new DataConversion();
        byte[] bmpFH = {'B', 'M', 0, 0, 0, 0, 0, 0, 0, 0, 54, 0, 0, 0};
        dataConversion.intToByteArray(bmpFH, 2, fileSize, 4);
        System.arraycopy(bmpFH, 0, bmpData, bmpDataOffset, bmpFH.length);
        bmpDataOffset += bmpFH.length;


        byte[] bmpIH = new byte[40];
        dataConversion.intToByteArray(bmpIH, 0, 40, 4);
        dataConversion.intToByteArray(bmpIH, 4, image.getWidth(), 4);
        dataConversion.intToByteArray(bmpIH, 8, image.getHeight(), 4);
        dataConversion.intToByteArray(bmpIH, 12, 2, 2);
        dataConversion.intToByteArray(bmpIH, 14, 24, 2);

        System.arraycopy(bmpIH, 0, bmpData, bmpDataOffset, bmpIH.length);
        bmpDataOffset += bmpIH.length;

        byte[] bmpPad = new byte[3];
        byte[] acc = new byte[bytesPerPx];
        for (int y = image.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < image.getWidth(); x++) {
                dataConversion.intToByteArray(acc, 0, table[y][x], bytesPerPx);
                System.arraycopy(acc, 0, bmpData, bmpDataOffset, bytesPerPx);
                bmpDataOffset += bytesPerPx;
            }
            System.arraycopy(bmpPad, 0, bmpData, bmpDataOffset, paddingCount);
            bmpDataOffset += paddingCount;
        }

        System.arraycopy(bmpFH, 0, bmpData, 0, 14);
        System.arraycopy(bmpIH, 0, bmpData, 14, 40);

        return bmpData;
    }
}
