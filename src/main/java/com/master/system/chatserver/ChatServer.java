package com.master.system.chatserver;

import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ChatServer {
    private static final Integer PORT = 7070;

    public static void main(String[] args) {
        final ConnectionRepository connectionRepository = new ConnectionRepository();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        log.info("Chat Server is Running on port {} ..", PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT, 10)) {
            while (true) {
                Socket socket = serverSocket.accept();
                CustomSocket mySocket = new CustomSocket(socket);
                connectionRepository.addConnection(String.valueOf(socket.getPort()), mySocket);
                log.info("Client List {}", connectionRepository.getClientList());
                CompletableFuture.runAsync(() -> new ChatHandler(connectionRepository, mySocket).run(), executor);
            }
        } catch (Exception ex) {
            log.error("Exception ", ex);
        }
    }

}
