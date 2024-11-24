import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Shared Bounded Buffer
class BoundedBuffer {
    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final int capacity;
    
    private final ReentrantLock lock = new ReentrantLock(); // Mutex
    private final Condition notEmpty = lock.newCondition(); // Condition for consumers
    private final Condition notFull = lock.newCondition();  // Condition for producers

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    // Add an item to the buffer (produced by producer)
    public void produce(int item) throws InterruptedException {
        lock.lock(); // Acquire lock
        try {
            // Wait if buffer is full
            while (buffer.size() == capacity) {
                notFull.await();
            }
            buffer.add(item); // Add item to the buffer
            System.out.println("Produced: " + item);
            notEmpty.signal(); // Notify consumers
        } finally {
            lock.unlock(); // Release lock
        }
    }

    // Remove an item from the buffer (consumed by consumer)
    public int consume() throws InterruptedException {
        lock.lock(); // Acquire lock
        try {
            // Wait if buffer is empty
            while (buffer.isEmpty()) {
                notEmpty.await();
            }
            int item = buffer.removeFirst(); // Remove item from the buffer
            System.out.println("Consumed: " + item);
            notFull.signal(); // Notify producers
            return item;
        } finally {
            lock.unlock(); // Release lock
        }
    }
}

// Producer class
class Producer implements Runnable {
    private final BoundedBuffer buffer;

    public Producer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            int item = 0; // Item to produce
            while (true) {
                buffer.produce(item++); // Produce an item
                Thread.sleep(500); // Simulate production time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Consumer class
class Consumer implements Runnable {
    private final BoundedBuffer buffer;

    public Consumer(BoundedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consume(); // Consume an item
                Thread.sleep(1000); // Simulate consumption time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main class to simulate Producer-Consumer problem
public class ProducerConsumer{
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer(5); // Buffer capacity of 5

        // Create producers and consumers
        Thread producer1 = new Thread(new Producer(buffer), "Producer 1");
        Thread producer2 = new Thread(new Producer(buffer), "Producer 2");
        Thread consumer1 = new Thread(new Consumer(buffer), "Consumer 1");
        Thread consumer2 = new Thread(new Consumer(buffer), "Consumer 2");

        // Start producers and consumers
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
    }
}
