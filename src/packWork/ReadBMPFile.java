package packWork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadBMPFile {
    private String fileName;

    public ReadBMPFile(String fileName) {
        this.fileName = fileName;
    }

    public static BMP readBinaryImageBMP(String fileName) {
        FileInputStream fileStream = null;

        try {
            fileStream = new FileInputStream(fileName);

            DataConversion dataConversion = new DataConversion();
            byte[] bmpFH = new byte[14];
            fileStream.read(bmpFH);

            byte[] bmpIH = new byte[40];
            fileStream.read(bmpIH);

            int w = dataConversion.convertBytesToInt(bmpIH, 4, 4);
            int h = dataConversion.convertBytesToInt(bmpIH, 8, 4);

            int bytesPerPx = dataConversion.convertBytesToInt(bmpIH, 14, 2) / 8;
            int paddingCount = (4 - (w * bytesPerPx) % 4) % 4;

            byte[] acc = new byte[w * bytesPerPx + paddingCount];
            int[][] table = new int[h][w];
            for (int y = h - 1; y >= 0; y--) {
                fileStream.read(acc);
                for (int x = 0; x < w; x++) {
                    int accOffset = x * bytesPerPx;
                    table[y][x] = dataConversion.convertBytesToInt(acc, accOffset, bytesPerPx);
                }
            }

            return new BMP(w, h, table);
        } catch (FileNotFoundException exception) {
            System.out.println("Finding unsuccessful");

        } catch (IOException exception) {
            System.out.println("Reading unsuccessful");

        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException exception) {
                    System.out.println("Closing unsuccessful");
                }
            }
        }

        return null;
    }
}
