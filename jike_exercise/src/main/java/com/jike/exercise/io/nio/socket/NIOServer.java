package com.jike.exercise.io.nio.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class NIOServer {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // open a selector
        Selector selector = Selector.open();
        log.info("start service in port 8888 ....");
        serverSocketChannel.bind(new InetSocketAddress(8888));
        // set non-blocking mode
        serverSocketChannel.configureBlocking(false);
        // register channel in selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            if(selector.select(2000) == 0) {
                log.info("there is no client connection, continue ...");
                continue;
            }
            // get the selectionKey in the channel
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    log.info("OP_ACCEPT");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()) {
                    log.info("OP_READ");
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    log.info("client send: {}", new String(buffer.array()));
                }

                keyIterator.remove();
            }
        }
    }
}
