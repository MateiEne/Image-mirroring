package packWork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReaderProducer implements Runnable {
    private static final int MAX_MESSAGE_SIZE = 1024 * 300; // 300Kb

    private final DataQueue dataQueue;
    private final String fileName;
    private boolean running = false;
    private FileInputStream fileInputStream;

    public FileReaderProducer(DataQueue dataQueue, String fileName) {
        this.dataQueue = dataQueue;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            fileInputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + fileName);

            return;
        }

        running = true;
        produce();
    }

    private void produce() {
        while (running) {
            if (dataQueue.isFull()) {
                try {
                    dataQueue.waitIsNotFull();
                } catch (InterruptedException e) {
                    System.out.println("system error");
                }
            }

            if (!running) {
                break;
            }

            try {
                Message message = generateMessage();
                dataQueue.add(message);

                if (message.getData() != null) {
                    System.out.println("Producer: Sent " + message.getData().length + " bytes from " + fileName);
                } else {
                    stop();
                }


                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                System.out.println("system error");
            }
        }
    }

    private Message generateMessage() throws IOException {
        byte[] data = new byte[MAX_MESSAGE_SIZE];
        int messageSize = fileInputStream.read(data);

        if (messageSize == -1) {
            return new Message(null);
        }

        byte[] result = new byte[messageSize];

        for (int i = 0; i < messageSize; i++) {
            result[i] = data[i];
        }

        return new Message(result);
    }

    public void stop() {
        running = false;
        dataQueue.notifyIsNotFull();

        try {
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("system error");
        }
    }
}
