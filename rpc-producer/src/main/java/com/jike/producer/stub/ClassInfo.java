package com.jike.producer.stub;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;



@Slf4j
public class ClassInfo implements Serializable {

    private String className;
    private String methodName;
    private Class<?>[] types;
    private Object[] objects;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}
