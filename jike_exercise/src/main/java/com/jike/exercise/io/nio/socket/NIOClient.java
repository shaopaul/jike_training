package com.jike.exercise.io.nio.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        if (!channel.connect(address)) {
            // if can't connect server, then continue to try with finishConnect
            while (!channel.finishConnect()) {
                log.info("still try to connect the server.");
            }
        }
        String msg = "hello, world";
        ByteBuffer writeBuf = ByteBuffer.wrap(msg.getBytes());
        channel.write(writeBuf);
        System.in.read();
    }
}
