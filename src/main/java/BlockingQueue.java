import java.util.ArrayDeque;
import java.util.Queue;

public class BlockingQueue<T> {
    private final Queue<T> queue = new ArrayDeque<>();
    private final int capacity;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Вместимость должна быть больше нуля"
            );
        }
        this.capacity = capacity;
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized void enqueue(T element) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }

        queue.add(element);
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        T element = queue.remove();
        notifyAll();
        return element;
    }
}