package packWork;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

public class FileReaderConsumer implements Runnable {
    private final DataQueue dataQueue;

    private final byte[] fileData = new byte[1024 * 1024 * 300]; // 300Mb

    private int fileDataSize = 0;

    private int fileDataIndex = 0;

    private boolean running = false;

    private final String outputFileName;

    public FileReaderConsumer(DataQueue dataQueue, String outputFileName) {
        this.dataQueue = dataQueue;
        this.outputFileName = outputFileName;
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

        WriteBMPFile.writeBinaryImageBMP(image, outputFileName);
    }
}
