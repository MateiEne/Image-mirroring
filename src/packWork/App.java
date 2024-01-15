package packWork;

public class App {
    public static void start(String inputFileName, String outputFileName) {
        DataQueue dataQueue = new DataQueue(2);

        FileReaderProducer fileReaderProducer = new FileReaderProducer(dataQueue, inputFileName);
        Thread producerThread = new Thread(fileReaderProducer);

        FileReaderConsumer fileReaderConsumer = new FileReaderConsumer(dataQueue, outputFileName);
        Thread consumerThread = new Thread(fileReaderConsumer);

        producerThread.start();
        consumerThread.start();
    }
}
