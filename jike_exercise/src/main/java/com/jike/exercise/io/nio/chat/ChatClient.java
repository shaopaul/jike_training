package com.jike.exercise.io.nio.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class ChatClient {

    private SocketChannel socketChannel;
    private String userName;

    public ChatClient() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
            if (!socketChannel.connect(address)) {
                while (!socketChannel.finishConnect()) {
                    log.info("still try to connect server ...");
                }
            }
            userName  = socketChannel.getRemoteAddress().toString();
            log.info("------ client {} is ready --------");
        } catch (IOException ioException) {
            log.error("fail to connect the server");
            ioException.printStackTrace();
        }
    }

    public void sendMsg(String msg) throws IOException {
        if ("bye".equalsIgnoreCase(msg)) {
            socketChannel.close();
            return;
        }

        msg = userName + " say: " + msg;
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(buffer);
    }

    public void receiveMsg() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(buffer);
        if (read > 0) {
            log.info(new String(buffer.array()));
        }
    }
}
