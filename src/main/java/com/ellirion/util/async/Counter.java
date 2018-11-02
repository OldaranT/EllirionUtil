package com.ellirion.util.async;

public class Counter {

    private final Object latch;
    private int count;

    /**
     * Construct a Counter with an initial count of zero.
     */
    public Counter() {
        this(0);
    }

    /**
     * Construct a Counter with an initial count of {@code count}.
     * @param count The initial count
     */
    public Counter(final int count) {
        this.latch = new Object();
        this.count = count;
    }

    /**
     * Increment the count by one.
     */
    public void increment() {
        synchronized (latch) {
            count++;
            latch.notifyAll();
        }
    }

    /**
     * Decrement the count by one.
     */
    public void decrement() {
        synchronized (latch) {
            count--;
            latch.notifyAll();
        }
    }

    /**
     * Get the current count.
     * @return The current count
     */
    public int get() {
        synchronized (latch) {
            return count;
        }
    }

    /**
     * Acquire the lock and perform the Runnable {@code r}.
     * @param r The Runnable to run
     */
    public void perform(Runnable r) {
        synchronized (latch) {
            r.run();
        }
    }

    /**
     * Wait for this Counter to reach zero.
     */
    public void await() {
        await(0);
    }

    /**
     * Wait for this Counter to reach {@code i}.
     * @param i The number to reach
     */
    public void await(int i) {
        try {
            synchronized (latch) {
                while (count != i) {
                    latch.wait();
                }
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("await() was interrupted", ex);
        }
    }
}
