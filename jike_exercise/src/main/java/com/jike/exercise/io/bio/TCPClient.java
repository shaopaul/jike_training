package com.jike.exercise.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


@Slf4j
public class TCPClient {

    public static void main(String[] args) throws IOException {
        while (true) {
            Socket s = new Socket("127.0.0.1", 8888);
            OutputStream os = s.getOutputStream();
            log.info("plz input: ");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            os.write(msg.getBytes(StandardCharsets.UTF_8));

            InputStream is = s.getInputStream();
            byte[] b = new byte[50];
            is.read(b);
            log.info("server say: {}", new String(b));

            s.close();
        }
    }
}
