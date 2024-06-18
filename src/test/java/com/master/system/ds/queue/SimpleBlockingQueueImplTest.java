package com.master.system.ds.queue;

import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.atomic.AtomicLong;

@ExtendWith(SpringExtension.class)
class SimpleBlockingQueueImplTest {

    BlockingQueue<Integer> blockingQueue;
    EasyRandom generator;

    @BeforeEach
    void setUp() throws InterruptedException {
        blockingQueue = new SimpleBlockingQueueImpl<>(10);
        generator = new EasyRandom();
    }

    @Test
    void testAbleToAddNumberIfRecordIsRemovedFromFilledQueue() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            blockingQueue.add(generator.nextInt(100));
        }
        AtomicLong startTime = new AtomicLong();
        AtomicLong endTime = new AtomicLong();

        Thread th1 = new Thread(() -> {
            startTime.set(System.currentTimeMillis());
            System.out.println("Attempt made for adding a value at: " + startTime.get());
            try {
                blockingQueue.add(1000);
                endTime.set(System.currentTimeMillis());
                System.out.println("Attempt successful for adding a value at: " + endTime.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Thread 1");
        th1.start();
        Thread.sleep(1000);
        Assertions.assertThat(th1.getState()).isEqualTo(Thread.State.WAITING);

        System.out.println("Record Fetched is " + blockingQueue.get());
        Thread.sleep(100);

        Assertions.assertThat(endTime.get() - startTime.get()).isGreaterThanOrEqualTo(1000);
    }

    @Test
    void testThreadIsWaitingIfNoRecordsArePresent() throws InterruptedException {
        AtomicLong startTime = new AtomicLong();
        AtomicLong endTime = new AtomicLong();

        Thread th2 = new Thread(() -> {
            startTime.set(System.currentTimeMillis());
            System.out.println("Attempt made for Getting a value at: " + startTime.get());
            try {
                Integer val = blockingQueue.get();
                endTime.set(System.currentTimeMillis());
                System.out.printf("Attempt Successful at %d and value received is %d", endTime.get(), val);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Thread 2");
        th2.start();

        Thread.sleep(1000);
        Assertions.assertThat(th2.getState()).isEqualTo(Thread.State.WAITING);

        int num = generator.nextInt(100);
        blockingQueue.add(num);
        System.out.printf("Record Added is %d\n", num);
        Thread.sleep(100);
        Assertions.assertThat(endTime.get() - startTime.get()).isGreaterThanOrEqualTo(1000);
    }
}