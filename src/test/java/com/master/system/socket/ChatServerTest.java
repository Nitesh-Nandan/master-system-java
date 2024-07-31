package com.master.system.socket;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.net.Socket;

@ExtendWith(SpringExtension.class)
class ChatServerTest {
    private static final String IP = "127.0.0.1";
    private static final Integer PORT = 7070;
    private static final String CL_RF = "\r\n";
    private EasyRandom easyRandom = new EasyRandom();

    @Test
    void streamReader() throws IOException {
        Socket socket = new Socket(IP, PORT);
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        int count = 1;
        while (count < 25) {
            String response = reader.readLine();
            System.out.println("Server response: " + response);
            String msg = String.format("%s-%s%s", easyRandom.nextInt(7000, 7010), easyRandom.nextObject(String.class), CL_RF);
            writer.write(msg);
            count++;
        }
    }
}
