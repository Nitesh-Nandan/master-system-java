package com.master.system.mutexdemo;

import lombok.Getter;

public class SharedCounter {

    @Getter
    private int counter = 0;
    private final Object mutex = new Object();

    public void increment() {
        counter  = counter + 1;
    }

    public void incrementWithMutex() {
        synchronized (mutex) {
            counter = counter + 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SharedCounter counter = new SharedCounter();

        int numThreads = 10;
        int incrementsPerThread = 10;

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.incrementWithMutex();
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Final counter value: " + counter.getCounter());
    }
}
