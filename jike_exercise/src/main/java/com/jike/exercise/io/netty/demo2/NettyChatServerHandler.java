package com.jike.exercise.io.netty.demo2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static List<Channel> channels = new CopyOnWriteArrayList<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        log.info("{} is online.", channel.remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(channel);
        log.info("{} is offline.", channel.remoteAddress().toString());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        log.info("server receive msg: {}", msg);
        Channel current = channelHandlerContext.channel();
        for (Channel dest : channels) {
            if (dest != current) {
                String text = current.remoteAddress().toString() + " say: " + msg;
                dest.writeAndFlush(text);
            }
        }
    }
}
