package com.master.system.chatserver;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class ChatClient {
    private static final Integer PORT = 7070;
    private static final String IP = "127.0.0.1";
    private static final String CL_RF = "\r\n";

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket(IP, PORT);
        CompletableFuture.runAsync(() -> {
            try {
                ChatClient.reader(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.runAsync(() -> {
            try {
                ChatClient.write(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        synchronized (ChatClient.class) {
            ChatClient.class.wait();
        }
    }

    private static void reader(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            String response = reader.readLine();
            System.out.println("Server response: " + response);
        }
    }

    private static void write(Socket socket) throws IOException {
        Scanner sc = new Scanner(System.in);
        BufferedWriter wb = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        while (true) {
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
