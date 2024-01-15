package packWork;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadBMPFile {
    private String fileName;

    private static int FH_SIZE;
    private static int IH_SIZE;

    static {
        FH_SIZE = 14;
        IH_SIZE = 40;
    }

    public ReadBMPFile(String fileName) {
        this.fileName = fileName;
    }

    public static BMP readBinaryImageBMP(BufferedInputStream dataStream) {
        try {
            BufferedInputStream fileStream = dataStream;

            DataConversion dataConversion = new DataConversion();
            byte[] bmpFH = new byte[FH_SIZE];
            fileStream.read(bmpFH);

            byte[] bmpIH = new byte[IH_SIZE];
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

        }

        return null;
    }
}
