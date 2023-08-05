package com.jike.exercise.io.nio.file;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class TestNIO {

    public static void main(String[] args) throws IOException {
        new TestNIO().writeFile();
        new TestNIO().readFile();
        new TestNIO().transferFile();
    }

    public void writeFile() throws IOException {
        FileOutputStream fos = new FileOutputStream("basic.txt");
        FileChannel channel = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String s = "hello, world";
        buffer.put(s.getBytes());
        buffer.flip();
        channel.write(buffer);
        fos.close();
    }

    public void readFile() throws IOException {
        File file = new File("basic.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel channel = fis.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        channel.read(buffer);
        log.info(new String(buffer.array()));
        fis.close();
    }

    public void transferFile() throws IOException {
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("basic_bak.txt");
        FileChannel srcChannel = fis.getChannel();
        FileChannel destChannel = fos.getChannel();
        srcChannel.transferTo(0, srcChannel.size(), destChannel);

        fis.close();
        fos.close();

    }
}
