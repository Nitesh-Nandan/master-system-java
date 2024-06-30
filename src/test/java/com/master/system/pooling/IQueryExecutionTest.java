package com.master.system.pooling;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
class IQueryExecutionTest {

    private IQueryExecution rawQueryExecutor;
    private static final String SLOW_QUERY = "SELECT SLEEP(0.5)";
    private static final String FAST_QUERY = "SELECT now()";

    @BeforeEach
    void setup() {
        rawQueryExecutor = new RawConnectionExecution();
    }


    @Test
    void testExecutionTimeTakenByRawThreadWhenSingleThreadIsRunning() throws SQLException, InterruptedException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            rawQueryExecutor.execute(FAST_QUERY);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken by Raw Connection: " + (endTime - startTime) + "ms");
    }

    @Test
    void testExecutionTimeTakenByPoolThreadWhenSingleThreadIsRunning() throws SQLException, InterruptedException {
        IQueryExecution poolExecutor = new ConnectionPoolExecution(5);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            poolExecutor.execute(FAST_QUERY);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken by pool Connection: " + (endTime - startTime) + "ms");
    }

    @Test
    void willRunWithoutErrorUsingRawConnectionForSmallConcurrentThread() throws InterruptedException, SQLException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        var futures = Stream.generate(() -> CompletableFuture.runAsync(() -> {
                    try {
                        rawQueryExecutor.execute(SLOW_QUERY);
                    } catch (InterruptedException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .limit(100)
                .toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");

        Assertions.assertThat(Boolean.TRUE).isTrue();
    }

    @Test
    void willPerformWorseIfThreadPoolSizeIsSmall() throws InterruptedException, SQLException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        IQueryExecution poolExecutor = new ConnectionPoolExecution(50);

        var futures = Stream.generate(() -> CompletableFuture.runAsync(() -> {
                    try {
                        poolExecutor.execute(SLOW_QUERY);
                    } catch (InterruptedException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .limit(100)
                .toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");

        Assertions.assertThat(Boolean.TRUE).isTrue();
    }

    @Test
    void willThrowExceptionSayingTooManyConnectionWhenRawConnectionIsUsed() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(300);

        var futures = Stream.generate(() -> CompletableFuture.runAsync(() -> {
                    try {
                        rawQueryExecutor.execute(SLOW_QUERY);
                    } catch (InterruptedException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .limit(300)
                .toList();


        Assertions.assertThatThrownBy(() -> CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join())
                .hasCauseInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(SQLException.class)
                .hasMessageContaining("Too many connections");

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");

        Assertions.assertThat(Boolean.TRUE).isTrue();
    }


    @Test
    void willRunWithoutErrorWhenConnectionPoolingIsUsed() throws InterruptedException, SQLException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IQueryExecution poolExecutor = new ConnectionPoolExecution(100);

        var futures = Stream.generate(() -> CompletableFuture.runAsync(() -> {
                    try {
                        poolExecutor.execute(SLOW_QUERY);
                    } catch (InterruptedException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .limit(1000)
                .toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + "ms");

        Assertions.assertThat(Boolean.TRUE).isTrue();
    }

}