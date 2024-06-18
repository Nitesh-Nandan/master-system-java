package com.master.system.ds.queue;

import java.util.LinkedList;
import java.util.List;

public class SimpleBlockingQueueImpl<E> implements BlockingQueue<E> {
    private final List<E> queue = new LinkedList<>();
    private final int limit;

    public SimpleBlockingQueueImpl(int limit) {
        this.limit = limit;
    }

    @Override
    public synchronized void add(E record) throws InterruptedException {
        while (queue.size() == limit) {
            wait();
        }
        queue.add(record);
        if (queue.size() == 1) {
            notifyAll();
        }
    }

    @Override
    public synchronized E get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        E record = queue.remove(0);
        if (queue.size() == limit - 1) {
            notifyAll();
        }
        return record;
    }
}
