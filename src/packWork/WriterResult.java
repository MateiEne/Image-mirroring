package packWork;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class WriterResult extends Thread {
    private final BufferedInputStream bufferedInputStream;

    private FileOutputStream fileOutputStream;

    private final String fileName;

    public WriterResult(BufferedInputStream bufferedInputStream, String fileName) {
        this.bufferedInputStream = bufferedInputStream;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        byte[] data = new byte[1024 * 1024 * 300]; // 300Mb

        try {
            fileOutputStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("cannaot create file " + fileName);

            return;
        }

        while (true) {
            try {
                int dataRead = bufferedInputStream.read(data);
                if (dataRead == -1) {
                    break;
                }

                System.out.println("WriterResult a primit " + dataRead + " bytes");

                fileOutputStream.write(data, 0, dataRead);
            } catch (Exception e) {
                System.out.println("system error");
            }
        }

        try {
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("system error");
        }
    }
}
