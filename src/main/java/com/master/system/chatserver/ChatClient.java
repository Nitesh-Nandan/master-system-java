package com.master.system.chatserver;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ChatClient {
    private static final Integer PORT = 7070;
    private static final String IP = "127.0.0.1";
    private static final String CL_RF = "\r\n";

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket(IP, PORT);
        CompletableFuture.runAsync(() -> {
            try {
                ChatClient.reader(socket);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.runAsync(() -> {
            try {
                ChatClient.write(socket);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        synchronized (ChatClient.class) {
            ChatClient.class.wait();
        }
    }

    private static void reader(Socket socket) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = null;
        while ((response = reader.readLine()) != null) {
            System.out.println("Server response: " + response);
            TimeUnit.MILLISECONDS.sleep(500);
        }
        System.out.println("Reader existed");
    }

    private static void write(Socket socket) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        BufferedWriter wb = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        while (true) {
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.print("Enter the Destination Port: ");
            int desPort = Integer.parseInt(sc.nextLine().trim());
            if (desPort == 0) {
                System.out.println("Exiting");
                return;
            }
            System.out.print("Enter Your Message: ");
            String input = sc.nextLine().trim();
            String message = String.format("%s-%s%s", desPort, input, CL_RF);
            System.out.println(message);
            wb.write(message);
            wb.flush();
        }
    }
}
