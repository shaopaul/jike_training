package com.jike.exercise.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
@Slf4j
public class TCPServer {

    public static void main(String[] args) throws IOException {
        log.info("server start in port 8888 ....");
        ServerSocket ss = new ServerSocket(8888);

        while (true) {
            Socket s = ss.accept();
            InputStream is = s.getInputStream();
            byte[] b = new byte[50];
            is.read(b);
            String clientIp = ss.getInetAddress().getHostAddress();
            log.info("{} say: {}", clientIp, new String(b).trim());
            OutputStream os = s.getOutputStream();
            os.write("get it".getBytes(StandardCharsets.UTF_8));
            s.close();
        }
    }
}
