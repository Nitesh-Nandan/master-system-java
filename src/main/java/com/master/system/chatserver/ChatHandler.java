package com.master.system.chatserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ChatHandler {
    private final ConnectionRepository connectionRepository;
    private final CustomSocket socket;

    public ChatHandler(ConnectionRepository connectionRepository, CustomSocket socket) {
        this.connectionRepository = connectionRepository;
        this.socket = socket;
    }

    public void run() {
        log.info("Received Connection from {}", socket.getPort());
        socket.sendMessage("Server", "You are connected from port " + socket.getPort());
        try {
            handle();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handle() throws IOException {
        String msg = null;
        while ((msg = socket.receiveMessage()) != null && !msg.isEmpty()) {
            try {
                log.info("Message Received {} from port {}", msg, socket.getPort());
                String[] records = msg.split("-");
                if (records.length < 2) {
                    socket.sendMessage(String.valueOf(socket.getPort()), "Message Format is wrong");
                }
                CustomSocket desSocket = connectionRepository.getConnection(records[0]);
                if (desSocket != null) {
                    desSocket.sendMessage(records[0], records[1]);
                    socket.sendMessage("Server: ", "Message Sent");
                } else {
                    socket.sendMessage(String.valueOf(socket.getPort()), "Connection doesn't exist");
                }
            } catch (Exception ex) {
                log.error("Error in sending message ", ex);
            }
        }
    }
}