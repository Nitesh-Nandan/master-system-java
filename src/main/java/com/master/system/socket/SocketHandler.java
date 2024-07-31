package com.master.system.socket;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SocketHandler {
    private static final String CL_RF = "\r\n";
    private final Socket clientSocket;
    private final InputStream in;
    private final OutputStream out;
    private final EasyRandom generator;

    public SocketHandler(Socket socket) throws IOException {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .stringLengthRange(50, 100);
        this.clientSocket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.generator = new EasyRandom(parameters);
        logSocketInfo();
    }

    public void readStream() throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String inputLine;
        try {
            while (clientSocket.isConnected() && (inputLine = br.readLine()) != null && !inputLine.isEmpty()) {
                String message = String.format("Received Record %s from port %d", inputLine, clientSocket.getPort());
                System.out.println(message);

                bw.write(String.format("Received Message %s %s", inputLine, CL_RF));
                bw.flush();
            }
        } catch (IOException e) {
            log.error("Error Occurred {} ", e.getMessage());
        } finally {
            clientSocket.close();
        }
        log.info("Is Connection Alive {} for Port {}", clientSocket.isConnected(), clientSocket.getPort());
    }

    public void streamData() throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            while (clientSocket.isConnected()) {
                String message = generator.nextObject(String.class);
                bw.write(String.format("Received Message %s %s", message, CL_RF));
                bw.flush();
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error Occurred {} ", e.getMessage());
        } finally {
            clientSocket.close();
        }
        log.info("Is Connection Alive {} for Port {}", clientSocket.isConnected(), clientSocket.getPort());
    }

    private void logSocketInfo() throws SocketException {
        log.info("Port = {}, LocalPort = {}, Socket = {}",
                clientSocket.getPort(), clientSocket.getLocalPort(), clientSocket);
    }
}