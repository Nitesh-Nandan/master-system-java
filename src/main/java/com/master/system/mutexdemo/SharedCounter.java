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
}
