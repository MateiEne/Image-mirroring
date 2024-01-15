package packWork;

import java.util.LinkedList;
import java.util.Queue;

public class DataQueue {
    private final Queue<Message> queue = new LinkedList<Message>();

    private final int maxSize;

    private final Object lockNotFull = new Object();
    private final Object lockNotEmpty = new Object();

    public DataQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isFull() {
        return queue.size() == maxSize;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void waitIsNotFull() throws InterruptedException {
        synchronized (lockNotFull) {
            lockNotFull.wait();
        }
    }

    public void notifyIsNotFull() {
        synchronized (lockNotFull) {
            lockNotFull.notify();
        }
    }

    public void waitIsNotEmpty() throws InterruptedException {
        synchronized (lockNotEmpty) {
            lockNotEmpty.wait();
        }
    }

    public void notifyIsNotEmpty() {
        synchronized (lockNotEmpty) {
            lockNotEmpty.notify();
        }
    }

    public void add(Message message) {
        queue.add(message);
        notifyIsNotEmpty();
    }

    public Message remove() {
        Message message = queue.poll();
        notifyIsNotFull();
        return message;
    }
}
