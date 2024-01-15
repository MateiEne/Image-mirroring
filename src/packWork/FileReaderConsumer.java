package packWork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileReaderConsumer implements Runnable {
    private final DataQueue dataQueue;

    private final byte[] fileData = new byte[1024 * 1024 * 300]; // 300Mb
    private final String outputFileName;
    private final BufferedOutputStream bufferedOutputStream;
    private int fileDataSize = 0;
    private int fileDataIndex = 0;
    private boolean running = false;

    public FileReaderConsumer(DataQueue dataQueue, String outputFileName, BufferedOutputStream bufferedOutputStream) {
        this.dataQueue = dataQueue;
        this.outputFileName = outputFileName;
        this.bufferedOutputStream = bufferedOutputStream;
    }

    @Override
    public void run() {
        running = true;
        consume();
    }

    private void consume() {
        while (running) {
            if (dataQueue.isEmpty()) {
                try {
                    dataQueue.waitIsNotEmpty();
                } catch (InterruptedException e) {
                    System.out.println("system error");
                }
            }

            if (!running) {
                break;
            }

            Message message = dataQueue.remove();

            if (message.getData() == null) {
                running = false;
                startImageProcessing();
                break;
            }

            useMessage(message);

            System.out.println("Consumer: Read " + message.getData().length + " bytes");
        }
    }

    private void useMessage(Message message) {
        for (byte b : message.getData()) {
            fileData[fileDataIndex++] = b;
        }

        fileDataSize = fileDataIndex;
    }

    public byte[] getFileData() {
        return fileData;
    }

    private void startImageProcessing() {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(fileData, 0, fileDataSize));

        BMP image = ReadBMPFile.readBinaryImageBMP(bufferedInputStream);

        if (image == null) {
            System.out.println("cannot read file input image");

            return;
        }

        ImageEditorWithMirroring imageEditor = new ImageEditorWithMirroring();
        imageEditor.setImage(image);
        imageEditor.mirrorBMP();

        image = imageEditor.getImage();

        sendImageToPipe(image);

        WriteBMPFile.writeBinaryImageBMP(image, outputFileName);
    }

    private void sendImageToPipe(BMP image) {
        byte[] imageBytes = WriteBMPFile.writeBinaryImageBMP(image);

        int startOffsetIndex = 0;

        for (int i = 0; i < 3; i++) {
            try {
                bufferedOutputStream.write(imageBytes, startOffsetIndex, imageBytes.length / 4);
                startOffsetIndex += imageBytes.length / 4;

                System.out.println("Consumer: Sent " + imageBytes.length / 4 + " bytes to pipe");
                Thread.sleep(1000);
            } catch (IOException e) {
                System.out.println("file write error");
            } catch (InterruptedException e) {
                System.out.println("system error");
            }
        }

        try {
            bufferedOutputStream.write(imageBytes, startOffsetIndex, imageBytes.length - startOffsetIndex);

            System.out.println("Consumer: Sent " + (imageBytes.length - startOffsetIndex) + " bytes to pipe");
            Thread.sleep(1000);
        } catch (IOException e) {
            System.out.println("file write error");
        } catch (InterruptedException e) {
            System.out.println("system error");
        }

        try {
            bufferedOutputStream.close();
        } catch (IOException e) {
            System.out.println("system error");
        }
    }
}
