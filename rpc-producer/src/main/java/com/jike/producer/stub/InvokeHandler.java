package com.jike.producer.stub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
public class InvokeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;
        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        Method method = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getTypes());
        Object result = method.invoke(clazz, classInfo.getObjects());
        ctx.writeAndFlush(result);
    }

    private String getImplClassName(ClassInfo classInfo) throws ClassNotFoundException {
        String interfacePath = "com.jike.producer.service";
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);
        Class superClass = Class.forName(interfacePath + interfaceName);

        Reflections reflections = new Reflections(interfacePath);
        Set<Class> implClassSet = reflections.getSubTypesOf(superClass);
        if (implClassSet.size() == 0) {
            log.info("didnt find impl class");
            return null;
        } else if (implClassSet.size() > 1) {
            log.info("find multiply impl");
            return null;
        } else {
            Class[] classes = implClassSet.toArray(new Class[0]);
            return  classes[0].getName();
        }
    }


}
