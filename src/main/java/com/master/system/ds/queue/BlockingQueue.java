package com.master.system.ds.queue;

public interface BlockingQueue<E> {
    void add(E record) throws InterruptedException;

    E get() throws InterruptedException;
}
