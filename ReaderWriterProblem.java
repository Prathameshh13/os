import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class ReadWriteLock {
    private int readers = 0; // Count of active readers
    private boolean writerActive = false; // Is a writer currently writing?
    
    private final ReentrantLock lock = new ReentrantLock(); // Mutex
    private final Condition condition = lock.newCondition(); // Condition variable

    // Reader lock
    public void lockRead() throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            // Wait until no writer is active
            while (writerActive) {
                condition.await();
            }
            readers++; // Increment reader count
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Reader unlock
    public void unlockRead() {
        lock.lock(); // Acquire the lock
        try {
            readers--; // Decrement reader count
            if (readers == 0) {
                condition.signalAll(); // Notify writers if no readers are left
            }
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Writer lock
    public void lockWrite() throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            // Wait until no readers or writer are active
            while (readers > 0 || writerActive) {
                condition.await();
            }
            writerActive = true; // Mark writer as active
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Writer unlock
    public void unlockWrite() {
        lock.lock(); // Acquire the lock
        try {
            writerActive = false; // Mark writer as inactive
            condition.signalAll(); // Notify waiting readers and writers
        } finally {
            lock.unlock(); // Release the lock
        }
    }
}

// Reader class
class Reader implements Runnable {
    private ReadWriteLock rwLock;
    private String readerName;

    public Reader(ReadWriteLock rwLock, String readerName) {
        this.rwLock = rwLock;
        this.readerName = readerName;
    }

    @Override
    public void run() {
        try {
            rwLock.lockRead(); // Acquire read lock
            System.out.println(readerName + " is reading...");
            Thread.sleep(1000); // Simulate reading time
            System.out.println(readerName + " finished reading.");
            rwLock.unlockRead(); // Release read lock
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Writer class
class Writer implements Runnable {
    private ReadWriteLock rwLock;
    private String writerName;

    public Writer(ReadWriteLock rwLock, String writerName) {
        this.rwLock = rwLock;
        this.writerName = writerName;
    }

    @Override
    public void run() {
        try {
            rwLock.lockWrite(); // Acquire write lock
            System.out.println(writerName + " is writing...");
            Thread.sleep(1000); // Simulate writing time
            System.out.println(writerName + " finished writing.");
            rwLock.unlockWrite(); // Release write lock
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Main class to simulate the Reader-Writer problem
public class ReaderWriterProblem {
    public static void main(String[] args) {
        ReadWriteLock rwLock = new ReadWriteLock();

        // Create readers and writers
        Thread reader1 = new Thread(new Reader(rwLock, "Reader 1"));
        Thread reader2 = new Thread(new Reader(rwLock, "Reader 2"));
        Thread writer1 = new Thread(new Writer(rwLock, "Writer 1"));
        Thread writer2 = new Thread(new Writer(rwLock, "Writer 2"));

        // Start readers and writers
        reader1.start();
        reader2.start();
        writer1.start();
        writer2.start();
    }
}
