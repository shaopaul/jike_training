package com.jike.sp.cat.servlet;

import java.util.List;
import java.util.Map;

public interface SPRequest {

    String getUri();
    String getPath();
    String getMethod();
    Map<String, List<String>> getParameters();
    List<String> getParameters(String name);
    String getParameter(String name);
}
