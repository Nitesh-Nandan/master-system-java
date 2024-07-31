package com.master.system.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

@Slf4j
public class SocketHandler {
    private static final String CL_RF = "\r\n";
    private final Socket clientSocket;
    private final InputStream in;
    private final OutputStream out;

    public SocketHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        logSocketInfo();
    }

    public void readStream() throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String inputLine;
        while (clientSocket.isConnected() && (inputLine = br.readLine()) != null && !inputLine.isEmpty()) {
            String message = String.format("Received Record %s from port %d", inputLine, clientSocket.getPort());
            System.out.println(message);

            bw.write(String.format("Received Message %s %s", inputLine, CL_RF));
            bw.flush();
        }
        clientSocket.close();
        log.info("Out from loop Port {}", clientSocket.getPort());
    }

    private void logSocketInfo() throws SocketException {
        log.info("Port = {}, LocalPort = {}, Socket = {}",
                clientSocket.getPort(), clientSocket.getLocalPort(), clientSocket);
    }


}