package com.master.system.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SocketServer {
    public static void main(String[] args) throws IOException {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(4221, 10)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                SocketHandler handler = new SocketHandler(clientSocket);
                CompletableFuture.runAsync(() -> {
                    try {
                        handler.streamData();
                    } catch (IOException e) {
                        log.error("Server Error {}", e.getMessage());
                    }
                }, executor);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
