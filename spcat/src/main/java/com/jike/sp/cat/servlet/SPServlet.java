package com.jike.sp.cat.servlet;

public abstract class SPServlet {

    public abstract void doGet(SPRequest request, SPResponse response) throws Exception;
    public abstract void doPost(SPRequest request, SPResponse response) throws Exception;

}
