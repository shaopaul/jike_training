package com.jike.sp.cat;

import com.jike.sp.cat.servlet.SPServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SPCatHandler extends ChannelInboundHandlerAdapter {

    private Map<String, SPServlet> nameToServletMap = new ConcurrentHashMap<>();
    private Map<String, String> nameToClassNameMap = new HashMap<>();

    public SPCatHandler(Map<String, SPServlet> nameToServletMap, Map<String, String> nameToClassNameMap) {
        this.nameToServletMap = nameToServletMap;
        this.nameToClassNameMap = nameToClassNameMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri();
            //get servlet name from uri
            String servletName = "";
            if (uri.contains("?") && uri.contains("/")) {
                servletName = uri.substring(uri.lastIndexOf("/") + 1, uri.indexOf("?"));
            }
            SPServlet servlet = new DefaultSPServlet();
            if (nameToServletMap.containsKey(servletName)) {
                servlet = nameToServletMap.get(servletName);
            } else if (nameToClassNameMap.containsKey(servletName)) {
                // double check
                if (nameToServletMap.get(servletName) == null) {
                    synchronized (this) {
                        if (nameToServletMap.get(servletName) == null) {
                            String className = nameToClassNameMap.get(servletName);
                            servlet = (SPServlet) Class.forName(className).newInstance();
                            nameToServletMap.put(servletName, servlet);
                        }
                    }
                }
            }

            HttpSPRequest httpSPRequest = new HttpSPRequest(request);
            HttpSPResponse httpSPResponse = new HttpSPResponse(request, ctx);
            if ("GET".equalsIgnoreCase(request.method().name())) {
                servlet.doGet(httpSPRequest, httpSPResponse);
            } else if ("POST".equalsIgnoreCase(request.method().name())) {
                servlet.doPost(httpSPRequest, httpSPResponse);
            }
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}