package com.jike.sp.cat;

import com.jike.sp.cat.servlet.SPServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SPCatServer {

    private String basePackage;
    private Map<String, SPServlet> nameToServletMap = new ConcurrentHashMap<>();
    private Map<String, String> nameToClassNameMap = new HashMap<>();

    public SPCatServer(String basePackage) {
        this.basePackage = basePackage;
    }

    public void start() throws DocumentException, InterruptedException {
        cacheClass(basePackage);
        runServer();
    }

    private void cacheClass(String basePackage) {
        URL resource = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        if (resource == null) {
            log.info("no resources found.");
            return;
        }

        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                cacheClass(basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String simpleClassName = file.getName().replace(".class", "").trim();
                log.info("find {}", basePackage + "." + simpleClassName);
                nameToClassNameMap.put(simpleClassName.toLowerCase(), basePackage + "." + simpleClassName);
            }
        }
    }

    private void runServer() throws DocumentException, InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // length of request queue
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new SPCatHandler(nameToServletMap, nameToClassNameMap));
                        }
                    });
            int port = initPort();
            ChannelFuture connection = b.bind(port).sync();
            log.info("SP Cat start on port {}", port);
            connection.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private int initPort() throws DocumentException {
        InputStream is = SPCatServer.class.getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(is);
        Element element = (Element) doc.selectSingleNode("//port");
        return Integer.valueOf(element.getText());
    }
}
