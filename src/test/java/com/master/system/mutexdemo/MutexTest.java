package com.master.system.mutexdemo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class MutexTest {

    private SharedCounter sharedCounter;

    @BeforeEach
    void setup() {
        sharedCounter = new SharedCounter();
    }

    @Test
    void testWithoutMutex() {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CompletableFuture<?>[] futures = new CompletableFuture[100000];

        for (int i = 0; i < 100000; i++) {
            futures[i] = CompletableFuture.runAsync(() -> {
                sharedCounter.increment();
            }, executorService);
        }

        CompletableFuture.allOf(futures).join();

        assertThat(sharedCounter.getCounter()).isNotEqualTo(100000);
        System.out.println("Actual counter value without mutex: " + sharedCounter.getCounter());

        executorService.shutdown();
    }

    @Test
    void testWithMutex() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CompletableFuture<?>[] futures = new CompletableFuture[100000];

        for (int i = 0; i < 100000; i++) {
            futures[i] = CompletableFuture.runAsync(() -> {
                sharedCounter.incrementWithMutex();
            }, executorService);
        }

        CompletableFuture.allOf(futures).join();

        // Now assert that the counter is equal to 100000
        assertThat(sharedCounter.getCounter()).isEqualTo(100000);

        executorService.shutdown();
    }
}