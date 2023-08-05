package com.jike.exercise.io.nio.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

@Slf4j
public class ChatServer {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public ChatServer() {
        try {
            log.info("starting server in port 8888 ...");
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(8888));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error("failed to start chat server");
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            while (true) {
                if (selector.select(2000) == 0) {
                    log.info("there is no client connection ...");
                    continue;
                }
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel sc = serverSocketChannel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        log.info("{} is online.", sc.getRemoteAddress().toString());
                    }

                    if (key.isReadable()) {
                        readMsg(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException ioException) {
            log.error("error in process the request.");
            ioException.printStackTrace();
        }
    }

    public void readMsg(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        if (read > 0) {
            log.info("[{}]: {}", new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()), new String(buffer.array()));
            broadCast(new String(buffer.array()), channel);
        }
    }

    public void broadCast(String msg, SocketChannel mainChannel) throws IOException {
        log.info("broadcasting msg ...");
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != mainChannel) {
                SocketChannel destChannel = (SocketChannel) targetChannel;
                ByteBuffer bufffer = ByteBuffer.wrap(msg.getBytes());
                destChannel.write(bufffer);
            }
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
