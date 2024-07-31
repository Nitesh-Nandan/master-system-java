package com.master.system.chatserver;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class CustomSocket {
    private static final String CL_RF = "\r\n";
    private final Socket socket;
    private final BufferedReader br;
    private final BufferedWriter bw;

    public CustomSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String senderInfo, String message) {
        if (isConnected()) {
            try {
                String msg = String.format("%s-%s%s", senderInfo, message, CL_RF);
                bw.write(msg);
                bw.flush();
            } catch (IOException e) {
                log.info("Error Occurred in sending message {} from Sender {}", message, senderInfo, e);
            }
        }
    }

    public String receiveMessage() {
        String line = null;
        if (isConnected()) {
            try {
                line = br.readLine();
            } catch (IOException e) {
                log.info("Error Occurred in Receiving message", e);
            }
        }
        return line;

    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public Integer getPort() {
        return socket.getPort();
    }
}
