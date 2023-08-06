package com.jike.webapp;

import com.jike.sp.cat.servlet.SPRequest;
import com.jike.sp.cat.servlet.SPResponse;
import com.jike.sp.cat.servlet.SPServlet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkuServlet extends SPServlet {

    @Override
    public void doGet(SPRequest request, SPResponse response) throws Exception {
        String uri = request.getUri();
        String name = request.getParameter("name");
        String content = "you are calling " + uri + " with param: " + name;
        response.write(content);
    }

    @Override
    public void doPost(SPRequest request, SPResponse response) throws Exception {
        doGet(request, response);
    }
}
