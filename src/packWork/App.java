package packWork;

import java.io.*;

public class App {
    public static void start(String inputFileName, String outputFileName) {
        DataQueue dataQueue = new DataQueue(5);

        PipedOutputStream pipedOutputStream = new PipedOutputStream();

        try {
            PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(pipedOutputStream);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(pipedInputStream);

            FileReaderProducer fileReaderProducer = new FileReaderProducer(dataQueue, inputFileName);
            Thread producerThread = new Thread(fileReaderProducer);

            FileReaderConsumer fileReaderConsumer = new FileReaderConsumer(dataQueue, outputFileName, bufferedOutputStream);
            Thread consumerThread = new Thread(fileReaderConsumer);

            producerThread.start();
            consumerThread.start();

            WriterResult writerResult = new WriterResult(bufferedInputStream, outputFileName);
            writerResult.start();
        } catch (IOException e) {
            System.out.println("system error");
        }
    }
}
