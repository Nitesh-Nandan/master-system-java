package com.master.system.socket;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class SocketClientTest {
    private static final String IP = "127.0.0.1";
    private static final Integer PORT = 4221;

    @Test
    void testConnection() throws IOException {
        Socket socket = new Socket(IP, PORT);
        Assertions.assertThat(socket.isConnected()).isTrue();


    }

    @Test
    void sendStream() throws IOException, InterruptedException {
        Socket socket = new Socket(IP, PORT);
        OutputStream output = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        int count = 1;
        while (count < 10) {
            String message = "Message " + count;
            writer.write(message);
            writer.newLine(); // Write a newline character to indicate the end of the message
            writer.flush(); // Ensure the message is sent immediately
            System.out.println("Sent: " + message);

            String response = reader.readLine();
            System.out.println("Server response: " + response);

            count++;
            TimeUnit.SECONDS.sleep(1); // Sleep for 1 second before sending the next request
        }
        output.close();
        input.close();
        socket.close();
    }

}