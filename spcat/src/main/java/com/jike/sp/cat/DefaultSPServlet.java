package com.jike.sp.cat;

import com.jike.sp.cat.servlet.SPRequest;
import com.jike.sp.cat.servlet.SPResponse;
import com.jike.sp.cat.servlet.SPServlet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSPServlet extends SPServlet {

    @Override
    public void doGet(SPRequest request, SPResponse response) throws Exception {
        String uri = request.getUri();
        String name = uri.substring(0, uri.lastIndexOf("?"));
        response.write("404 - no this servlet: " + name);
    }

    @Override
    public void doPost(SPRequest request, SPResponse response) throws Exception {
        doGet(request, response);
    }
}
